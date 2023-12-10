package ru.yamost.playlistmaker.search.data.network

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.yamost.playlistmaker.search.data.NetworkClient
import ru.yamost.playlistmaker.search.data.network.dto.Response
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchRequest
import ru.yamost.playlistmaker.search.ui.SearchFragment

class RetrofitNetworkClient(private val tracksService: TracksService) : NetworkClient {
    override suspend fun doSearchTrackRequest(dto: TrackSearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                Log.v(SearchFragment::class.java.simpleName, "Retrofit doSearchTrackRequest")
                val response = tracksService.getTracksBySearchQuery(searchQuery = dto.text)
                    .apply { resultCode = 200 }
                Log.v(SearchFragment::class.java.simpleName, "Retrofit requestSuccess")
                response
            } catch (e: CancellationException) {
                e.printStackTrace()
                Log.v(SearchFragment::class.java.simpleName, "Retrofit cancel return")
                Response().apply { resultCode = NetworkClient.CANCEL_REQUEST_CODE }
            } catch (e: Exception) {
                e.printStackTrace()
                Response().apply { resultCode = 500 }
            }
        }
    }
}