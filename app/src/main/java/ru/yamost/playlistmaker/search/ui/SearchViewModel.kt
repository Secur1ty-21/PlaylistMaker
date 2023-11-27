package ru.yamost.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.yamost.playlistmaker.creator.Creator
import ru.yamost.playlistmaker.search.domain.api.TracksInteractor
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.search.ui.model.SearchScreenState
import ru.yamost.playlistmaker.util.Resource

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val historyInteractor =
        Creator.provideSearchHistoryInteractor(application.applicationContext)
    private val searchInteractor = Creator.provideTracksInteractor()
    private var lastSentSearchQuery = ""
    private var searchQuery = ""
    private var historyTrackList = historyInteractor.getHistory()
    private val trackList = mutableListOf<Track>()
    private val searchScreenState = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    private val handler = Handler(Looper.getMainLooper())
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
                } else if (trackList.isNotEmpty()) {
                    searchScreenState.value = SearchScreenState.Content(trackList)
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
            trackList.clear()
            searchScreenState.value = SearchScreenState.Loading
            searchInteractor.searchTracks(text = searchQuery,
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(result: Resource<List<Track>, SearchErrorStatus>) {
                        when (result) {
                            is Resource.Success -> {
                                trackList.addAll(result.data)
                                searchScreenState.postValue(SearchScreenState.Content(result.data))
                            }

                            is Resource.Error -> {
                                trackList.clear()
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

        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(application)
                }
            }
    }
}