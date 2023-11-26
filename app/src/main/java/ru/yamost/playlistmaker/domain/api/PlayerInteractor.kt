package ru.yamost.playlistmaker.domain.api

import ru.yamost.playlistmaker.domain.model.PlayerInteractorState
import ru.yamost.playlistmaker.domain.model.PlayerState

interface PlayerInteractor {
    val playedTime: Int
    val currentState: PlayerState
    fun prepare(consumer: PlayerConsumer)
    fun play()
    fun pause()
    fun clearMemory()
    fun restore(playerInteractorState: PlayerInteractorState)
    fun formatPlayedTime(): String
    interface PlayerConsumer {
        fun onReadyForUse()
        fun onTrackEnd()
    }
}