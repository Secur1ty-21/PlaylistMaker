package ru.yamost.playlistmaker.playlist.domain.model

import android.net.Uri

data class Playlist(
    val id: Int,
    val imageUri: Uri?,
    val name: String,
    val description: String,
    val size: Int
)