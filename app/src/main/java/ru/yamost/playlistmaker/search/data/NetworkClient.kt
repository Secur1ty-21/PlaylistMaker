package ru.yamost.playlistmaker.search.data

import ru.yamost.playlistmaker.search.data.network.dto.Response
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchRequest

interface NetworkClient {
    companion object {
        const val CANCEL_REQUEST_CODE = -500
    }
    suspend fun doSearchTrackRequest(dto: TrackSearchRequest): Response
}