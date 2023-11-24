package ru.yamost.playlistmaker.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Track(
    val id: Int,
    val name: String,
    val artist: String,
    val time: String,
    val artworkUrl: String,
    val collection: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Parcelable
