package ru.yamost.playlistmaker.player.presentation.model

import androidx.annotation.DrawableRes

sealed interface PlayerScreenState {
    data class PlayButtonState(
        @DrawableRes val drawableRes: Int,
        val isEnabled: Boolean = false,
        val playedTime: String? = null
    ) : PlayerScreenState

    data class PlayedTime(val playedTime: String) : PlayerScreenState
}