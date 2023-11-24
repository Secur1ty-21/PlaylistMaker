package ru.yamost.playlistmaker.domain.impl

import ru.yamost.playlistmaker.domain.api.TrackRepository
import ru.yamost.playlistmaker.domain.api.TracksInteractor
import ru.yamost.playlistmaker.domain.model.SearchStatus
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val repository: TrackRepository
) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(text: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            try {
                val result = repository.searchTracks(searchQuery = text)
                consumer.consume(
                    trackList = result.first, searchStatus = result.second
                )
            } catch (e: Exception) {
                e.printStackTrace()
                consumer.consume(
                    trackList = emptyList(), searchStatus = SearchStatus.CONNECTION_ERROR
                )
            }
        }
    }
}