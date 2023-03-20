package ru.yamost.playlistmaker.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Track(
    @SerializedName("trackId") val id: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    @SerializedName("artworkUrl100") val artworkUrl: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
) : Parcelable
