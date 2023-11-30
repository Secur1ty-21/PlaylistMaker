package ru.yamost.playlistmaker.player.data

import android.media.MediaPlayer
import ru.yamost.playlistmaker.player.domain.api.PlayerController
import ru.yamost.playlistmaker.player.domain.model.PlayerState

class PlayerControllerImpl(
    private val mediaPlayer: MediaPlayer
) : PlayerController {
    override var currentState = PlayerState.DEFAULT
    override val currentPosition: Int
        get() = mediaPlayer.currentPosition

    override fun prepare(
        trackUrl: String,
        onReadyListener: () -> Unit,
        onEndTrackListener: () -> Unit,
        onErrorPrepared: () -> Unit
    ) {
        with(mediaPlayer) {
            setDataSource(trackUrl)
            setOnPreparedListener {
                currentState = PlayerState.PREPARED
                onReadyListener()
            }
            setOnCompletionListener {
                currentState = PlayerState.PREPARED
                onEndTrackListener()
            }
            setOnErrorListener { _, _, _ ->
                onErrorPrepared()
                true
            }
            prepareAsync()
        }
    }

    override fun play() {
        if (currentState == PlayerState.PREPARED || currentState == PlayerState.PAUSED) {
            mediaPlayer.start()
            currentState = PlayerState.PLAYING
        }
    }

    override fun pause() {
        if (currentState == PlayerState.PLAYING) {
            currentState = PlayerState.PAUSED
            mediaPlayer.pause()
        }
    }

    override fun seekTo(time: Int) {
        if (time <= 0) return
        if (currentState == PlayerState.PREPARED || currentState == PlayerState.PAUSED) {
            mediaPlayer.seekTo(time)
        }
    }

    override fun release() {
        mediaPlayer.release()
    }
}