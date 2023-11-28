package ru.yamost.playlistmaker.search.ui.model

import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track

sealed class SearchScreenState {
    object Default: SearchScreenState()
    object Loading : SearchScreenState()
    data class Error(val searchState: SearchErrorStatus): SearchScreenState()
    data class Content(val trackList: List<Track>): SearchScreenState()
    data class History(val trackList: List<Track>): SearchScreenState()
}