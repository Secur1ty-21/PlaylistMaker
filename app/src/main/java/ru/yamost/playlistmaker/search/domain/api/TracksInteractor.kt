package ru.yamost.playlistmaker.search.domain.api

import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.util.Resource

interface TracksInteractor {
    fun searchTracks(text: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(result: Resource<List<Track>, SearchErrorStatus>)
    }
}