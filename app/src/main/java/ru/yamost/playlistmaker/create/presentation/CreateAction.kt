package ru.yamost.playlistmaker.create.presentation

import ru.yamost.playlistmaker.playlist.domain.model.Playlist

sealed interface CreateAction {
    data class ShowAcceptedDialog(
        val onPosBtnClick: () -> Unit,
        val onCancelBtnClick: () -> Unit
    ) : CreateAction

    data object NavigateBack : CreateAction
    data class NavigateBackWithResult(val albumName: String) : CreateAction
    data class SetUiWithPlaylist(val playlist: Playlist): CreateAction
}