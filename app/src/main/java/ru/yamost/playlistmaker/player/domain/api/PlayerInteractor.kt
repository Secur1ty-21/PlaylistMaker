package ru.yamost.playlistmaker.player.domain.api

import ru.yamost.playlistmaker.player.domain.model.PlayerState

interface PlayerInteractor {
    val currentState: PlayerState
    fun prepare(trackUrl: String, consumer: PlayerConsumer)
    fun play()
    fun pause()
    fun clearMemory()
    fun playedTime(): Int
    interface PlayerConsumer {
        fun onReadyForUse()
        fun onTrackEnd()
        fun onError()
    }

    companion object {
        const val MAX_AVAILABLE_TRACK_DURATION = 30_000L
        const val START_TRACK_TIME = 0L
    }
}