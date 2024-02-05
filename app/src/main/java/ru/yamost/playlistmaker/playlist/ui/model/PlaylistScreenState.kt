package ru.yamost.playlistmaker.playlist.ui.model

import ru.yamost.playlistmaker.playlist.domain.model.Playlist

sealed interface PlaylistScreenState {
    data class Content(val playlistList: List<Playlist>) : PlaylistScreenState
    data object Empty : PlaylistScreenState
}