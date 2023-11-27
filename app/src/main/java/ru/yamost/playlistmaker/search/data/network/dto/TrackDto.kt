package ru.yamost.playlistmaker.search.data.network.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TrackDto(
    @SerializedName("trackId") val id: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    @SerializedName("artworkUrl100") val artworkUrl: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)
