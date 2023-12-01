package ru.yamost.playlistmaker.search.domain.api

import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTracks(searchQuery: String): Resource<List<Track>, SearchErrorStatus>

    fun cancelSearchRequest()
}