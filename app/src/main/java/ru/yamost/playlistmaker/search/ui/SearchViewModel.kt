package ru.yamost.playlistmaker.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yamost.playlistmaker.search.domain.api.SearchHistoryInteractor
import ru.yamost.playlistmaker.search.domain.api.TracksInteractor
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.search.ui.model.SearchScreenState
import ru.yamost.playlistmaker.util.Resource

class SearchViewModel(
    private val historyInteractor: SearchHistoryInteractor,
    private val searchInteractor: TracksInteractor
) : ViewModel() {
    private var lastRequestWithReceivedResponse = ""
    private var searchQuery = ""
    private var historyTrackList = historyInteractor.getHistory()
    private val searchScreenState = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    private val isClickDebounced = MutableLiveData(true)
    private var debounceRequest: Job? = null

    fun observeIsClickDebounce(): LiveData<Boolean> = isClickDebounced
    fun getSearchScreenState(): LiveData<SearchScreenState> = searchScreenState

    fun debounceClick() {
        viewModelScope.launch {
            delay(ITEM_CLICKED_DEBOUNCE_DELAY)
            isClickDebounced.value = true
        }
    }

    fun onSearchTextChangedEvent(searchQuery: String) {
        this.searchQuery = searchQuery
        debounceRequest?.cancel()
        Log.v(SearchFragment::class.java.simpleName, "onSearchTextChangedEvent $searchQuery")
        searchScreenState.value?.let {
            if (it is SearchScreenState.Error && it.searchState == SearchErrorStatus.CANCELED) return
        }
        when {
            searchQuery.isNotEmpty() -> {
                if (searchQuery != lastRequestWithReceivedResponse) {
                    searchScreenState.value = SearchScreenState.Default
                    debounceRequest = viewModelScope.launch {
                        delay(SEARCH_DEBOUNCE_DELAY)
                        sentSearchRequest(searchQuery)
                    }
                } else if (searchInteractor.lastFondedTrackList.isNotEmpty()) {
                    searchScreenState.value =
                        SearchScreenState.Content(searchInteractor.lastFondedTrackList)
                }
            }

            isTimeToShowSearchHistory() -> {
                searchScreenState.value = SearchScreenState.History(historyTrackList)
            }

            searchQuery.isEmpty() -> {
                searchScreenState.value = SearchScreenState.Default
            }
        }
    }

    fun onFocusChangedEvent(hasFocus: Boolean) {
        if (hasFocus && isTimeToShowSearchHistory()) {
            searchScreenState.value = SearchScreenState.History(historyTrackList)
        }
    }

    fun runPreviousSearchRequest(searchQuery: String) {
        debounceRequest = viewModelScope.launch {
            sentSearchRequest(searchQuery, repeatRequest = true)
        }
    }

    private suspend fun sentSearchRequest(searchQuery: String, repeatRequest: Boolean = false) {
        Log.v(SearchFragment::class.java.simpleName, "sentSearchRequest $searchQuery $repeatRequest")
        if (searchQuery != lastRequestWithReceivedResponse || repeatRequest) {
            searchScreenState.value = SearchScreenState.Loading
            searchInteractor.searchTracks(text = searchQuery).collect { result ->
                Log.v(SearchFragment::class.java.simpleName, "resultViewModel $result")
                lastRequestWithReceivedResponse = searchQuery
                when (result) {
                    is Resource.Success -> {
                        searchScreenState.postValue(SearchScreenState.Content(result.data))
                    }

                    is Resource.Error -> {
                        searchScreenState.postValue(SearchScreenState.Error(result.errorStatus))
                    }
                }
            }
        }
    }

    fun saveTrackToHistory(track: Track) {
        historyInteractor.saveTrack(track)
        historyTrackList = historyInteractor.getHistory()
        if (searchScreenState.value is SearchScreenState.History) {
            searchScreenState.value = SearchScreenState.History(historyTrackList)
        }
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        historyTrackList = emptyList()
        searchScreenState.value = SearchScreenState.Default
    }

    private fun isTimeToShowSearchHistory(): Boolean {
        return searchQuery.isEmpty() && historyInteractor.isHistoryExist()
    }

    fun onNavigateAction() {
        debounceRequest?.cancel()
        if (debounceRequest?.isCancelled == true) {
            Log.v(SearchFragment::class.java.simpleName, "onNavigation cancel")
            searchScreenState.value = SearchScreenState.Error(SearchErrorStatus.CANCELED)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val ITEM_CLICKED_DEBOUNCE_DELAY = 1000L
    }
}