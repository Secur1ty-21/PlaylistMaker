package ru.yamost.playlistmaker.playlistDetail.presentation.model

import ru.yamost.playlistmaker.playlist.domain.model.Playlist

sealed interface PlaylistDetailAction {
    data object ShowEmptyPlaylistMessage : PlaylistDetailAction
    data object NavigateBack : PlaylistDetailAction
    data class NavigateToEditScreen(val playlistId: Int) : PlaylistDetailAction
    data class UpdatePlaylist(val playlist: Playlist) : PlaylistDetailAction
}