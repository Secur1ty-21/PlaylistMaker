package ru.yamost.playlistmaker.search.domain.impl

import ru.yamost.playlistmaker.search.domain.api.TrackRepository
import ru.yamost.playlistmaker.search.domain.api.TracksInteractor
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Resource
import java.util.concurrent.ExecutorService

class TrackInteractorImpl(
    private val repository: TrackRepository,
    private val executor: ExecutorService
) : TracksInteractor {
    private var _lastFoundedTrackList = emptyList<Track>()
    override val lastFondedTrackList: List<Track>
        get() = _lastFoundedTrackList

    override fun searchTracks(text: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            try {
                val result = repository.searchTracks(searchQuery = text)
                _lastFoundedTrackList = when(result) {
                    is Resource.Error -> emptyList()
                    is Resource.Success -> result.data
                }
                consumer.consume(result)
            } catch (e: Exception) {
                e.printStackTrace()
                consumer.consume(Resource.Error(SearchErrorStatus.CONNECTION_ERROR, null))
            }
        }
    }
}