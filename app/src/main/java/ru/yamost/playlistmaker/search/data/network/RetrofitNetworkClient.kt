package ru.yamost.playlistmaker.search.data.network

import retrofit2.Call
import ru.yamost.playlistmaker.search.data.NetworkClient
import ru.yamost.playlistmaker.search.data.network.dto.Response
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchRequest

class RetrofitNetworkClient(private val tracksService: TracksService) : NetworkClient {
    private var sentRequest: Call<out Any>? = null
    override fun doSearchTrackRequest(dto: TrackSearchRequest): Response {
        return run {
            val request = tracksService.getTracksBySearchQuery(searchQuery = dto.text)
            sentRequest = request
            val response = request.execute()
            sentRequest = null
            val body = response.body() ?: Response()
            body.apply { resultCode = response.code() }
        }
    }

    override fun cancelRequest() {
        sentRequest?.cancel()
    }
}