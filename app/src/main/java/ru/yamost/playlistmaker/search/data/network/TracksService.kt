package ru.yamost.playlistmaker.search.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchResponse

interface TracksService {
    @GET("/search?entity=song")
    suspend fun getTracksBySearchQuery(@Query("term") searchQuery: String): TrackSearchResponse
}