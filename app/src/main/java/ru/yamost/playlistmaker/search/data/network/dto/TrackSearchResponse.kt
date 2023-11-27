package ru.yamost.playlistmaker.search.data.network.dto

import com.google.gson.annotations.SerializedName

class TrackSearchResponse(
    @SerializedName("results") val trackList: List<TrackDto>
) : Response()