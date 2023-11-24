package ru.yamost.playlistmaker.domain.api

import ru.yamost.playlistmaker.domain.model.Track
import ru.yamost.playlistmaker.domain.model.SearchStatus

interface TracksInteractor {
    fun searchTracks(text: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(trackList: List<Track>, searchStatus: SearchStatus)
    }
}