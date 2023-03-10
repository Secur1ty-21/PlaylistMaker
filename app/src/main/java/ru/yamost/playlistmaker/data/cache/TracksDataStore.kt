package ru.yamost.playlistmaker.data.cache

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.yamost.playlistmaker.data.model.Track
import ru.yamost.playlistmaker.data.network.ApiProvider
import ru.yamost.playlistmaker.data.network.ResponseTrackList
import ru.yamost.playlistmaker.data.network.ResultCallback

object TracksDataStore {

    fun getTracksBySearchQuery(
        searchQuery: String,
        resultCallback: ResultCallback<List<Track>>
    ): Call<ResponseTrackList> {
        val request = ApiProvider.tracksService.getTracksBySearchQuery(searchQuery)
        request.enqueue(object : Callback<ResponseTrackList> {
            override fun onResponse(
                call: Call<ResponseTrackList>,
                response: Response<ResponseTrackList>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    resultCallback.onSuccess(response.body()!!.trackList)
                } else {
                    resultCallback.onFailure(error = IllegalArgumentException("Server exception"))
                }
            }

            override fun onFailure(call: Call<ResponseTrackList>, t: Throwable) {
                resultCallback.onFailure(error = t)
            }
        })
        return request
    }
}