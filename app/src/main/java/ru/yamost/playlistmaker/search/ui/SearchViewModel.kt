package ru.yamost.playlistmaker.search.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.yamost.playlistmaker.search.domain.api.SearchHistoryInteractor
import ru.yamost.playlistmaker.search.domain.api.TracksInteractor
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.search.ui.model.SearchScreenState
import ru.yamost.playlistmaker.util.Resource

class SearchViewModel(
    private val historyInteractor: SearchHistoryInteractor,
    private val searchInteractor: TracksInteractor,
    private val handler: Handler
) : ViewModel() {
    private var lastSentSearchQuery = ""
    private var searchQuery = ""
    private var historyTrackList = historyInteractor.getHistory()
    private val searchScreenState = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    private val searchRequest = Runnable {
        sentSearchRequest(searchQuery)
    }

    fun onSearchTextChangedEvent(searchQuery: String) {
        handler.removeCallbacks(searchRequest)
        this.searchQuery = searchQuery
        when {
            searchQuery.isNotEmpty() -> {
                if (searchQuery != lastSentSearchQuery) {
                    searchScreenState.value = SearchScreenState.Default
                    handler.postDelayed(searchRequest, SEARCH_DEBOUNCE_DELAY)
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

    fun runPreviousSearchRequest() {
        sentSearchRequest(lastSentSearchQuery, repeatRequest = true)
    }

    private fun sentSearchRequest(searchQuery: String, repeatRequest: Boolean = false) {
        if (searchQuery != lastSentSearchQuery || repeatRequest) {
            lastSentSearchQuery = searchQuery
            searchScreenState.value = SearchScreenState.Loading
            searchInteractor.searchTracks(text = searchQuery,
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(result: Resource<List<Track>, SearchErrorStatus>) {
                        when (result) {
                            is Resource.Success -> {
                                searchScreenState.postValue(SearchScreenState.Content(result.data))
                            }

                            is Resource.Error -> {
                                searchScreenState.postValue(SearchScreenState.Error(result.errorStatus))
                            }
                        }
                    }
                })
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

    fun getSearchScreenState(): LiveData<SearchScreenState> = searchScreenState

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}