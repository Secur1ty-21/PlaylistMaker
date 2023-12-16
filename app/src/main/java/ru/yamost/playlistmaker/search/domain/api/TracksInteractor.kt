package ru.yamost.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Resource

interface TracksInteractor {
    val lastFondedTrackList: List<Track>
    fun searchTracks(text: String): Flow<Resource<List<Track>, SearchErrorStatus>>
}