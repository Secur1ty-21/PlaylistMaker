package ru.yamost.playlistmaker.create.presentation

import android.net.Uri

sealed interface CreateEvent {
    data class OnAlbumNameType(val albumName: String) : CreateEvent
    data class OnAlbumDescriptionType(val albumDescription: String) : CreateEvent
    data class OnPhotoCaptured(val bitmap: Uri) : CreateEvent
    data object OnBtnCreateClick : CreateEvent
    data object OnBackRequested : CreateEvent
}