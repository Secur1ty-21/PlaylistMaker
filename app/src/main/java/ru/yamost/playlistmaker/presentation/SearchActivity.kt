package ru.yamost.playlistmaker.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import retrofit2.Call
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.data.cache.TracksDataStore
import ru.yamost.playlistmaker.data.model.Track
import ru.yamost.playlistmaker.data.network.ResponseTrackList
import ru.yamost.playlistmaker.data.network.ResultCallback
import ru.yamost.playlistmaker.presentation.adapter.TrackListAdapter

@SuppressLint("NotifyDataSetChanged")
class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_INPUT_TEXT = "SEARCH_INPUT_TEXT"
    }

    private var searchInputText = ""
    private val tracksList = mutableListOf<Track>()
    private val trackListAdapter = TrackListAdapter(tracksList)
    private lateinit var tracksRecycler: RecyclerView
    private lateinit var errorBlock: LinearLayout
    private lateinit var errorMessage: TextView
    private lateinit var errorIcon: ImageView
    private lateinit var refreshButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var clearButton: ImageButton
    private lateinit var topAppBar: MaterialToolbar
    private var requestGetTracksBySearchQuery: Call<ResponseTrackList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()
        setListeners()
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

        tracksRecycler = findViewById(R.id.track_list)
        tracksRecycler.adapter = trackListAdapter
    }

    private fun setListeners() {
        val textListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearButton.isVisible = false
                } else {
                    searchInputText = s.toString()
                    clearButton.isVisible = true
                }
            }
        }
        searchEditText.addTextChangedListener(textListener)
        topAppBar.setNavigationOnClickListener { finish() }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            onSearchEditorAction(actionId, searchEditText)
        }
        clearButton.setOnClickListener { onClickClearButton() }
        refreshButton.setOnClickListener { updateTrackListBySearchQuery(searchInputText) }
    }

    private fun onSearchEditorAction(actionId: Int, view: View): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            closeKeyboard(view)
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
        tracksList.clear()
        trackListAdapter.notifyDataSetChanged()
        closeKeyboard(searchEditText)
    }

    private fun updateTrackListBySearchQuery(searchQuery: String) {
        tracksList.clear()
        trackListAdapter.notifyDataSetChanged()
        progressBar.isVisible = true
        requestGetTracksBySearchQuery =
            TracksDataStore.getTracksBySearchQuery(searchQuery = searchQuery,
                resultCallback = object : ResultCallback<List<Track>> {
                    override fun onSuccess(data: List<Track>) {
                        if (data.isEmpty()) {
                            updateUiWithSearchStatus(SearchStatus.EMPTY_RESULT)
                        } else {
                            updateUiWithSearchStatus(SearchStatus.SUCCESS, data)
                        }
                    }

                    override fun onFailure(error: Throwable) {
                        updateUiWithSearchStatus(SearchStatus.CONNECTION_ERROR)
                    }
                }
            )
    }

    private fun updateUiWithSearchStatus(
        searchStatus: SearchStatus,
        data: List<Track> = emptyList()
    ) {
        progressBar.isVisible = false
        refreshButton.isVisible = false
        when (searchStatus) {
            SearchStatus.SUCCESS -> {
                errorBlock.isVisible = false
                tracksList.addAll(data)
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.search_text).setText(
            savedInstanceState.getString(
                SEARCH_INPUT_TEXT, ""
            )
        )
        if (searchInputText.isNotEmpty()) {
            updateTrackListBySearchQuery(searchInputText)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT_TEXT, searchInputText)
    }

    override fun onDestroy() {
        super.onDestroy()
        requestGetTracksBySearchQuery?.cancel()
    }
}