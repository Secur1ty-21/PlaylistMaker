package ru.yamost.playlistmaker.player.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.favorites.domain.api.FavoriteTrackRepository
import ru.yamost.playlistmaker.player.domain.api.PlayerController
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.model.PlayerState
import ru.yamost.playlistmaker.search.domain.model.Track

class PlayerInteractorImpl(
    private val playerController: PlayerController,
    private val favoriteTrackRepository: FavoriteTrackRepository
) : PlayerInteractor {
    override val currentState: PlayerState
        get() = playerController.currentState

    override fun prepare(trackUrl: String, consumer: PlayerInteractor.PlayerConsumer) {
        playerController.prepare(
            trackUrl = trackUrl,
            onReadyListener = { consumer.onReadyForUse() },
            onEndTrackListener = { consumer.onTrackEnd() },
            onErrorPrepared = { consumer.onError() }
        )
    }

    override fun play() {
        playerController.play()
    }

    override fun pause() {
        playerController.pause()
    }

    override fun clearMemory() {
        playerController.release()
    }

    override fun playedTime(): Int {
        return playerController.currentPosition
    }

    override suspend fun addTrackToFavorite(track: Track) {
        favoriteTrackRepository.saveTrack(track)
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        favoriteTrackRepository.deleteTrack(track)
    }

    override suspend fun isTrackInFavorite(track: Track): Flow<Boolean> {
        return favoriteTrackRepository.isTrackExist(track)
    }
}