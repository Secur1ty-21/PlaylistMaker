package ru.yamost.playlistmaker.player.domain.api

import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.player.domain.model.PlayerState
import ru.yamost.playlistmaker.search.domain.model.Track

interface PlayerInteractor {
    val currentState: PlayerState
    fun prepare(trackUrl: String, consumer: PlayerConsumer)
    fun play()
    fun pause()
    fun clearMemory()
    fun playedTime(): Int
    suspend fun addTrackToFavorite(track: Track)
    suspend fun deleteTrackFromFavorite(track: Track)
    suspend fun isTrackInFavorite(track: Track): Flow<Boolean>
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