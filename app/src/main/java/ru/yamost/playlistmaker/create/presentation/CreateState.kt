package ru.yamost.playlistmaker.create.presentation

import android.net.Uri

data class CreateState(
    val albumName: String = "",
    val albumDescription: String = "",
    val photo: Uri? = null,
    val size: Int? = null
)