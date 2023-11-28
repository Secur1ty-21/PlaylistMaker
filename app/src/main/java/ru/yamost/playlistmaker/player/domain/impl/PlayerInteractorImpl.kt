package ru.yamost.playlistmaker.player.domain.impl

import ru.yamost.playlistmaker.player.domain.api.PlayerController
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.model.PlayerState
import java.text.SimpleDateFormat

class PlayerInteractorImpl(
    private val formatter: SimpleDateFormat,
    private val playerController: PlayerController,
) : PlayerInteractor {
    companion object {
        private const val MAX_AVAILABLE_TRACK_DURATION = 30_000L
    }

    override val formatAvailableTrackDuration: String =
        formatter.format(MAX_AVAILABLE_TRACK_DURATION)
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

    override fun formatPlayedTime(): String {
        return formatter.format(playerController.currentPosition)
    }
}