package ru.yamost.playlistmaker.search.data.network

import ru.yamost.playlistmaker.search.data.NetworkClient
import ru.yamost.playlistmaker.search.data.network.dto.Response
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchRequest

class RetrofitNetworkClient(private val tracksService: TracksService) : NetworkClient {
    override fun doSearchTrackRequest(dto: TrackSearchRequest): Response {
        return run {
            val response = tracksService.getTracksBySearchQuery(searchQuery = dto.text).execute()
            val body = response.body() ?: Response()
            body.apply { resultCode = response.code() }
        }
    }
}