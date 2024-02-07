package ru.yamost.playlistmaker.create.presentation

sealed interface CreateAction {
    data class ShowAcceptedDialog(
        val onPosBtnClick: () -> Unit,
        val onCancelBtnClick: () -> Unit
    ) : CreateAction

    data object NavigateBack : CreateAction
    data class NavigateBackWithResult(val albumName: String) : CreateAction
}