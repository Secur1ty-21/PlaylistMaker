package ru.yamost.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import ru.yamost.playlistmaker.Creator
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.domain.api.SearchHistoryInteractor
import ru.yamost.playlistmaker.domain.api.TracksInteractor
import ru.yamost.playlistmaker.domain.model.SearchStatus
import ru.yamost.playlistmaker.domain.model.Track
import ru.yamost.playlistmaker.presentation.search.TrackListAdapter
import ru.yamost.playlistmaker.ui.player.PlayerActivity

@SuppressLint("NotifyDataSetChanged")
class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val ITEM_CLICKED_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_INPUT_TEXT = "SEARCH_INPUT_TEXT"
        private const val SEARCH_HISTORY_FILE_NAME = "Search history"
        const val TRACK_ITEM_KEY = "Track item"
    }

    private lateinit var tracksRecycler: RecyclerView
    private lateinit var errorBlock: LinearLayout
    private lateinit var errorMessage: TextView
    private lateinit var errorIcon: ImageView
    private lateinit var refreshButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var clearButton: ImageButton
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var hintSearchHistory: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var historyInteractor: SearchHistoryInteractor
    private lateinit var handler: Handler
    private lateinit var searchInteractor: TracksInteractor
    private var searchInputText = ""
    private var isTrackItemClickAllowed = true
    private val trackList = mutableListOf<Track>()
    private val trackListAdapter = TrackListAdapter(trackList)
    private val searchRequest = Runnable {
        closeKeyboard(searchEditText)
        updateTrackListBySearchQuery(searchInputText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()
        setListeners()
        initObjects()
    }

    private fun initViews() {
        clearButton = findViewById(R.id.button_clear)
        topAppBar = findViewById(R.id.topAppBar)
        searchEditText = findViewById(R.id.search_text)
        refreshButton = findViewById(R.id.refresh_button)
        errorBlock = findViewById(R.id.block_error)
        errorMessage = findViewById(R.id.message_error)
        errorIcon = findViewById(R.id.icon_error)
        progressBar = findViewById(R.id.progress_bar)
        hintSearchHistory = findViewById(R.id.searchHistoryHint)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)

        tracksRecycler = findViewById(R.id.track_list)
        tracksRecycler.adapter = trackListAdapter
    }

    private fun setListeners() {
        val textListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(searchRequest)
                handler.postDelayed(searchRequest, SEARCH_DEBOUNCE_DELAY)
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearButton.isVisible = false
                    searchInputText = ""

                    if (searchEditText.hasFocus()) {
                        handler.removeCallbacks(searchRequest)
                        errorBlock.isVisible = false
                        showSearchHistory()
                    } else {
                        hideSearchHistory()
                    }
                } else {
                    hideSearchHistory()
                    searchInputText = s.toString()
                    clearButton.isVisible = true
                }
            }
        }
        searchEditText.addTextChangedListener(textListener)
        searchEditText.setOnFocusChangeListener { _, _ -> showSearchHistory() }
        topAppBar.setNavigationOnClickListener { finish() }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            onSearchEditorAction(actionId, searchEditText)
        }
        clearButton.setOnClickListener { onClickClearButton() }
        refreshButton.setOnClickListener { onClickRefreshButton() }
        clearHistoryButton.setOnClickListener { onClickClearHistoryButton() }
        trackListAdapter.itemClickListener = { track -> onClickTrackItem(track) }
    }

    private fun initObjects() {
        historyInteractor = Creator.provideSearchHistoryInteractor(
            getSharedPreferences(SEARCH_HISTORY_FILE_NAME, MODE_PRIVATE)
        )
        searchInteractor = Creator.provideTracksInteractor()
        handler = Handler(Looper.getMainLooper())
    }

    private fun onSearchEditorAction(actionId: Int, view: View): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            closeKeyboard(view)
            handler.removeCallbacks(searchRequest)
            updateTrackListBySearchQuery(searchInputText)
            return true
        }
        return false
    }

    private fun closeKeyboard(view: View) {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun onClickClearButton() {
        searchEditText.setText("")
        searchInputText = ""
        trackList.clear()
        trackListAdapter.notifyDataSetChanged()
        showSearchHistory()
        closeKeyboard(searchEditText)
    }

    private fun onClickRefreshButton() {
        errorBlock.isVisible = false
        updateTrackListBySearchQuery(searchInputText)
    }

    private fun updateTrackListBySearchQuery(searchQuery: String) {
        if (searchQuery.isNotEmpty()) {
            trackList.clear()
            trackListAdapter.notifyDataSetChanged()
            errorBlock.isVisible = false
            progressBar.isVisible = true
            searchInteractor.searchTracks(
                text = searchQuery,
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(trackList: List<Track>, searchStatus: SearchStatus) {
                        runOnUiThread {
                            updateUiWithSearchStatus(searchStatus, trackList)
                        }
                    }
                }
            )
        }
    }

    private fun updateUiWithSearchStatus(searchStatus: SearchStatus, data: List<Track>) {
        progressBar.isVisible = false
        refreshButton.isVisible = false
        when (searchStatus) {
            SearchStatus.SUCCESS -> {
                errorBlock.isVisible = false
                trackList.addAll(data)
                trackListAdapter.notifyDataSetChanged()
            }

            SearchStatus.EMPTY_RESULT -> {
                showError(R.drawable.ic_not_found, R.string.search_not_found)
            }

            SearchStatus.CONNECTION_ERROR -> {
                showError(R.drawable.ic_no_connection, R.string.search_no_connection)
                refreshButton.isVisible = true
            }
        }
    }

    private fun showError(@DrawableRes iconId: Int, @StringRes textId: Int) {
        errorBlock.isVisible = true
        errorIcon.setImageResource(iconId)
        errorMessage.text = getString(textId)
    }

    private fun onClickClearHistoryButton() {
        historyInteractor.clearHistory()
        hideSearchHistory()
    }

    private fun onClickTrackItem(track: Track) {
        if (debounceTrackItemClick()) {
            historyInteractor.saveTrack(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(
                TRACK_ITEM_KEY,
                track.copy(
                    artworkUrl = track.artworkUrl
                        .replaceAfterLast('/', "512x512bb.jpg")
                )
            )
            startActivity(intent)
        }
    }

    private fun debounceTrackItemClick(): Boolean {
        val current = isTrackItemClickAllowed
        if (current) {
            isTrackItemClickAllowed = false
            handler.postDelayed({ isTrackItemClickAllowed = true }, ITEM_CLICKED_DEBOUNCE_DELAY)
            return true
        }
        return false
    }

    private fun hideSearchHistory() {
        trackList.clear()
        trackListAdapter.notifyDataSetChanged()
        hintSearchHistory.isVisible = false
        clearHistoryButton.isVisible = false
    }

    private fun showSearchHistory() {
        if (isTimeToShowSearchHistory()) {
            errorBlock.isVisible = false
            trackList.clear()
            trackList.addAll(historyInteractor.getHistory())
            trackListAdapter.notifyDataSetChanged()
            hintSearchHistory.isVisible = true
            clearHistoryButton.isVisible = true
        }
    }

    private fun isTimeToShowSearchHistory(): Boolean {
        return searchEditText.hasFocus() && searchInputText.isEmpty()
                && historyInteractor.isHistoryExist()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.search_text).setText(
            savedInstanceState.getString(
                SEARCH_INPUT_TEXT, ""
            )
        )
        handler.removeCallbacks(searchRequest)
        if (searchInputText.isNotEmpty()) {
            updateTrackListBySearchQuery(searchInputText)
        } else if (!errorBlock.isVisible) {
            showSearchHistory()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT_TEXT, searchInputText)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRequest)
    }
}