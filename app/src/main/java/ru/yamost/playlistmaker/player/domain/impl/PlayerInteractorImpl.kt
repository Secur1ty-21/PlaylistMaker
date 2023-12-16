package ru.yamost.playlistmaker.player.domain.impl

import ru.yamost.playlistmaker.player.domain.api.PlayerController
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.model.PlayerState

class PlayerInteractorImpl(
    private val playerController: PlayerController,
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
}