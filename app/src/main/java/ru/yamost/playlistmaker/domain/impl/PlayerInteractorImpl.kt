package ru.yamost.playlistmaker.domain.impl

import ru.yamost.playlistmaker.domain.api.PlayerController
import ru.yamost.playlistmaker.domain.api.DateTimeRepository
import ru.yamost.playlistmaker.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.domain.model.PlayerInteractorState
import ru.yamost.playlistmaker.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

class PlayerInteractorImpl(
    dateTimeRepository: DateTimeRepository,
    private val playerController: PlayerController,
): PlayerInteractor {
    private val formatter = SimpleDateFormat(
        dateTimeRepository.getTrackTimeFormat(),
        dateTimeRepository.getPreferredLocale()
    )
    private val executor = Executors.newCachedThreadPool()
    override val playedTime: Int
        get() = playerController.currentPosition
    override val currentState: PlayerState
        get() = playerController.currentState

    override fun prepare(consumer: PlayerInteractor.PlayerConsumer) {
        executor.execute {
            playerController.prepare(
                { consumer.onReadyForUse() },
                { consumer.onTrackEnd() }
            )
        }
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

    override fun restore(playerInteractorState: PlayerInteractorState) {
        playerController.currentState = playerInteractorState.playerState
        playerController.seekTo(playerInteractorState.playedTime)
    }

    override fun formatPlayedTime(): String {
        return formatter.format(playedTime)
    }
}