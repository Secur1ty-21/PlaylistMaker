package ru.yamost.playlistmaker.search.domain.impl

import ru.yamost.playlistmaker.search.domain.api.TrackRepository
import ru.yamost.playlistmaker.search.domain.api.TracksInteractor
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val repository: TrackRepository
) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(text: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            try {
                val result = repository.searchTracks(searchQuery = text)
                consumer.consume(result)
            } catch (e: Exception) {
                e.printStackTrace()
                consumer.consume(Resource.Error(SearchErrorStatus.CONNECTION_ERROR, null))
            }
        }
    }
}