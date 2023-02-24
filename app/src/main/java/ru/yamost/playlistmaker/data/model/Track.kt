package ru.yamost.playlistmaker.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    @SerializedName("artworkUrl100") val artworkUrl: String?
)
