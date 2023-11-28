package ru.yamost.playlistmaker.search.data.network

import ru.yamost.playlistmaker.search.data.NetworkClient
import ru.yamost.playlistmaker.search.data.network.dto.Response
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchRequest

class RetrofitNetworkClient : NetworkClient {
    override fun doSearchTrackRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            val response = RetrofitApiProvider.tracksService
                .getTracksBySearchQuery(searchQuery = dto.text)
                .execute()
            val body = response.body() ?: Response()
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}