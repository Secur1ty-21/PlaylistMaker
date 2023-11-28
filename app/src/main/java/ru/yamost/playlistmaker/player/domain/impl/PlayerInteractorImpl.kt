package ru.yamost.playlistmaker.player.domain.impl

import ru.yamost.playlistmaker.player.domain.api.PlayerController
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.model.PlayerState
import ru.yamost.playlistmaker.search.domain.api.DateTimeRepository
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

class PlayerInteractorImpl(
    dateTimeRepository: DateTimeRepository,
    private val playerController: PlayerController,
) : PlayerInteractor {
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
                { consumer.onTrackEnd() },
                { consumer.onError() }
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

    override fun formatPlayedTime(): String {
        return formatter.format(playedTime)
    }
}