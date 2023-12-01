package ru.yamost.playlistmaker.search.data

import ru.yamost.playlistmaker.search.data.network.dto.Response
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchRequest

interface NetworkClient {
    fun doSearchTrackRequest(dto: TrackSearchRequest): Response
    fun cancelRequest()
}