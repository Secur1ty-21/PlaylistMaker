package ru.yamost.playlistmaker.domain.api

import ru.yamost.playlistmaker.domain.model.PlayerState

interface PlayerController {
    val currentPosition: Int
    var currentState: PlayerState
    fun prepare(
        onReadyListener: () -> Unit, onEndTrackListener: () -> Unit
    )

    fun play()
    fun pause()
    fun seekTo(time: Int)
    fun release()
}