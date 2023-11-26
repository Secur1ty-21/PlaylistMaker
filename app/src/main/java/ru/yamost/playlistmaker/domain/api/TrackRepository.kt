package ru.yamost.playlistmaker.domain.api

import ru.yamost.playlistmaker.domain.model.SearchStatus
import ru.yamost.playlistmaker.domain.model.Track

interface TrackRepository {
    fun searchTracks(searchQuery: String): Pair<List<Track>, SearchStatus>
}