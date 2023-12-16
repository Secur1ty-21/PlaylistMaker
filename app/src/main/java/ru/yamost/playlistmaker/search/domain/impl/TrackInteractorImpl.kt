package ru.yamost.playlistmaker.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.yamost.playlistmaker.search.domain.api.TrackRepository
import ru.yamost.playlistmaker.search.domain.api.TracksInteractor
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Resource

class TrackInteractorImpl(
    private val repository: TrackRepository,
) : TracksInteractor {
    private var _lastFoundedTrackList = emptyList<Track>()
    override val lastFondedTrackList: List<Track>
        get() = _lastFoundedTrackList

    override fun searchTracks(text: String): Flow<Resource<List<Track>, SearchErrorStatus>> {
        return repository.searchTracks(text).map { result ->
            _lastFoundedTrackList = when (result) {
                is Resource.Error -> emptyList()
                is Resource.Success -> result.data
            }
            result
        }
    }
}