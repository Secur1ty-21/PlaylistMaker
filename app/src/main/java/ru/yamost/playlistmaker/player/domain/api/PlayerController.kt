package ru.yamost.playlistmaker.player.domain.api

import ru.yamost.playlistmaker.player.domain.model.PlayerState

interface PlayerController {
    val currentPosition: Int
    var currentState: PlayerState
    fun prepare(
        onReadyListener: () -> Unit,
        onEndTrackListener: () -> Unit,
        onErrorPrepared: () -> Unit
    )

    fun play()
    fun pause()
    fun seekTo(time: Int)
    fun release()
}