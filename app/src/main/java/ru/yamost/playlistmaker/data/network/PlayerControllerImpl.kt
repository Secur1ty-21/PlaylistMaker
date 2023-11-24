package ru.yamost.playlistmaker.data.network

import android.media.MediaPlayer
import ru.yamost.playlistmaker.domain.api.PlayerController
import ru.yamost.playlistmaker.domain.model.PlayerState

class PlayerControllerImpl(
    private val trackUrl: String,
) : PlayerController {
    private val mediaPlayer by lazy { MediaPlayer() }
    override var currentState = PlayerState.DEFAULT
    override val currentPosition: Int
        get() = mediaPlayer.currentPosition

    override fun prepare(
        onReadyListener: () -> Unit,
        onEndTrackListener: () -> Unit
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
            prepare()
        }
    }

    override fun play() {
        if (currentState == PlayerState.PREPARED ||
            currentState == PlayerState.PAUSED) {
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
        if (currentState == PlayerState.PREPARED ||
            currentState == PlayerState.PAUSED) {
            mediaPlayer.seekTo(time)
        }
    }

    override fun release() {
        mediaPlayer.release()
    }
}