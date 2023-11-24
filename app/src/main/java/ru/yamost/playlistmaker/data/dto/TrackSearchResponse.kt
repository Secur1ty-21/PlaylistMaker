package ru.yamost.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

class TrackSearchResponse(
    @SerializedName("results") val trackList: List<TrackDto>
) : Response()