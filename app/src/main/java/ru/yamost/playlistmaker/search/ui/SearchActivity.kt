package ru.yamost.playlistmaker.search.ui

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
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.databinding.ActivitySearchBinding
import ru.yamost.playlistmaker.player.ui.PlayerActivity
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.search.ui.model.SearchScreenState

@SuppressLint("NotifyDataSetChanged")
class SearchActivity : AppCompatActivity() {
    companion object {
        private const val ITEM_CLICKED_DEBOUNCE_DELAY = 1000L
        const val TRACK_ITEM_KEY = "Track item"
    }

    private lateinit var handler: Handler
    private lateinit var binding: ActivitySearchBinding
    private var isTrackItemClickAllowed = true
    private val viewModel by viewModel<SearchViewModel>()
    private val trackList = mutableListOf<Track>()
    private val trackListAdapter = TrackListAdapter(trackList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())
        binding.trackList.adapter = trackListAdapter
        setListeners()
        viewModel.getSearchScreenState().observe(this) {
            render(it)
        }
    }

    private fun setListeners() {
        val textListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchTextChangedEvent(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
                binding.buttonClear.isVisible = s?.isNotEmpty() == true
            }
        }
        binding.searchText.addTextChangedListener(textListener)
        binding.searchText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusChangedEvent(hasFocus)
        }
        binding.topAppBar.setNavigationOnClickListener { finish() }
        binding.searchText.setOnEditorActionListener { _, actionId, _ ->
            onSearchEditorAction(actionId)
        }
        binding.buttonClear.setOnClickListener { onClickClearButton() }
        binding.refreshButton.setOnClickListener { onClickRefreshButton() }
        binding.clearHistoryButton.setOnClickListener { viewModel.clearHistory() }
        trackListAdapter.itemClickListener = { track -> onClickTrackItem(track) }
    }

    private fun render(state: SearchScreenState) {
        clearPrevState()
        when (state) {
            is SearchScreenState.Loading -> {
                closeKeyboard(binding.searchText)
                binding.progressBar.isVisible = true
            }

            is SearchScreenState.History -> {
                showSearchHistory(state.trackList)
            }

            is SearchScreenState.Content -> {
                updateTrackList(state.trackList)
            }

            is SearchScreenState.Error -> {
                showError(state.searchState)
            }

            is SearchScreenState.Default -> {}
        }
    }

    private fun clearPrevState() {
        binding.blockError.isVisible = false
        binding.refreshButton.isVisible = false
        binding.progressBar.isVisible = false
        hideSearchHistory()
        updateTrackList(emptyList())
    }

    private fun onSearchEditorAction(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            closeKeyboard(binding.searchText)
            viewModel.onSearchTextChangedEvent(binding.searchText.text.toString())
            return true
        }
        return false
    }

    private fun onClickClearButton() {
        binding.searchText.setText("")
        viewModel.onSearchTextChangedEvent("")
        closeKeyboard(binding.searchText)
    }

    private fun onClickRefreshButton() {
        binding.blockError.isVisible = false
        viewModel.runPreviousSearchRequest()
    }

    private fun showError(searchErrorStatus: SearchErrorStatus) {
        when (searchErrorStatus) {
            SearchErrorStatus.EMPTY_RESULT -> {
                showError(R.drawable.ic_not_found, R.string.search_not_found)
            }

            SearchErrorStatus.CONNECTION_ERROR -> {
                showError(R.drawable.ic_no_connection, R.string.search_no_connection)
                binding.refreshButton.isVisible = true
            }
        }
    }

    private fun showError(@DrawableRes iconId: Int, @StringRes textId: Int) {
        binding.blockError.isVisible = true
        binding.iconError.setImageResource(iconId)
        binding.messageError.text = getString(textId)
    }

    private fun hideSearchHistory() {
        binding.searchHistoryHint.isVisible = false
        binding.clearHistoryButton.isVisible = false
    }

    private fun showSearchHistory(tracks: List<Track>) {
        if (binding.searchText.hasFocus()) {
            updateTrackList(tracks)
            binding.blockError.isVisible = false
            binding.searchHistoryHint.isVisible = true
            binding.clearHistoryButton.isVisible = true
        }
    }

    private fun onClickTrackItem(track: Track) {
        if (debounceTrackItemClick()) {
            viewModel.saveTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_ITEM_KEY, track)
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

    private fun updateTrackList(tracks: List<Track>) {
        trackList.clear()
        trackList.addAll(tracks)
        trackListAdapter.notifyDataSetChanged()
    }

    private fun closeKeyboard(view: View) {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}