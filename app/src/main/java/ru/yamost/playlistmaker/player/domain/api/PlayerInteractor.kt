package ru.yamost.playlistmaker.player.domain.api

import ru.yamost.playlistmaker.player.domain.model.PlayerState

interface PlayerInteractor {
    val currentState: PlayerState
    val formatAvailableTrackDuration: String
    val formatStartTrackTime: String
    fun prepare(trackUrl: String, consumer: PlayerConsumer)
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