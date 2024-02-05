package ru.yamost.playlistmaker.player.presentation.model

sealed interface PlayerAction {
    data class ShowSnackbar(val isTrackAdded: Boolean, val playlistName: String): PlayerAction
}