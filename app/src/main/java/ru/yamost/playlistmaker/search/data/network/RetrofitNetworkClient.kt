package ru.yamost.playlistmaker.search.data.network

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.yamost.playlistmaker.search.data.NetworkClient
import ru.yamost.playlistmaker.search.data.network.dto.Response
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchRequest

class RetrofitNetworkClient(private val tracksService: TracksService) : NetworkClient {
    override suspend fun doSearchTrackRequest(dto: TrackSearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = tracksService.getTracksBySearchQuery(searchQuery = dto.text)
                    .apply { resultCode = 200 }
                response
            } catch (e: CancellationException) {
                e.printStackTrace()
                Response().apply { resultCode = NetworkClient.CANCEL_REQUEST_CODE }
            } catch (e: Exception) {
                e.printStackTrace()
                Response().apply { resultCode = 500 }
            }
        }
    }
}