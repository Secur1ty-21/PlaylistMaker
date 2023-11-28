package ru.yamost.playlistmaker.player.domain.api

import ru.yamost.playlistmaker.player.domain.model.PlayerState

interface PlayerController {
    val currentPosition: Int
    val currentState: PlayerState
    fun prepare(
        trackUrl: String,
        onReadyListener: () -> Unit,
        onEndTrackListener: () -> Unit,
        onErrorPrepared: () -> Unit
    )

    fun play()
    fun pause()
    fun seekTo(time: Int)
    fun release()
}