package ru.yamost.playlistmaker.player.domain.api

import ru.yamost.playlistmaker.player.domain.model.PlayerState

interface PlayerInteractor {
    val playedTime: Int
    val currentState: PlayerState
    fun prepare(consumer: PlayerConsumer)
    fun play()
    fun pause()
    fun clearMemory()
    fun formatPlayedTime(): String
    interface PlayerConsumer {
        fun onReadyForUse()
        fun onTrackEnd()
        fun onError()
    }
}