package ru.yamost.playlistmaker.player.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.model.PlayerState
import ru.yamost.playlistmaker.player.ui.model.PlayerScreenState
import ru.yamost.playlistmaker.search.domain.model.Track

class PlayerViewModel(
    track: Track?,
    private val interactor: PlayerInteractor,
    private val handler: Handler
) : ViewModel() {
    private val pauseDrawableRes = R.drawable.ic_pause_circle
    private val playDrawableRes = R.drawable.ic_play_circle
    private val _trackInfoState = MutableLiveData<Track?>(
        track?.copy(
            releaseDate = track.releaseDate.substringBefore('-'),
            artworkUrl = track.artworkUrl.replaceAfterLast('/', "512x512bb.jpg")
        )
    )
    val trackInfoState: LiveData<Track?> get() = _trackInfoState
    private val _playerScreenState = MutableLiveData<PlayerScreenState>(
        PlayerScreenState.PlayedTime(interactor.formatAvailableTrackDuration)
    )
    val playerScreenState: LiveData<PlayerScreenState> get() = _playerScreenState
    private val updateTimeProgress = object : Runnable {
        override fun run() {
            if (interactor.currentState == PlayerState.PLAYING) {
                _playerScreenState.value = PlayerScreenState.PlayedTime(
                    playedTime = interactor.formatPlayedTime(),
                )
                handler.postDelayed(this, UPDATE_TIME_INTERVAL)
            }
        }
    }

    init {
        preparePlayer(track?.previewUrl ?: "")
    }

    private fun preparePlayer(trackUrl: String) {
        _playerScreenState.value = PlayerScreenState.PlayButtonState(
            drawableRes = playDrawableRes, isEnabled = false
        )
        interactor.prepare(trackUrl, object : PlayerInteractor.PlayerConsumer {
            override fun onReadyForUse() {
                _playerScreenState.value = PlayerScreenState.PlayButtonState(
                    drawableRes = playDrawableRes,
                    isEnabled = true,
                    playedTime = interactor.formatAvailableTrackDuration
                )
            }

            override fun onTrackEnd() {
                _playerScreenState.value = PlayerScreenState.PlayButtonState(
                    drawableRes = playDrawableRes, isEnabled = true, interactor.formatPlayedTime()
                )
            }

            override fun onError() {
                preparePlayer(trackUrl)
            }
        })
    }

    fun onPlayButton() {
        if (interactor.currentState == PlayerState.PREPARED || interactor.currentState == PlayerState.PAUSED) {
            play()
        } else if (interactor.currentState == PlayerState.PLAYING) {
            pause()
        }
    }

    private fun play() {
        interactor.play()
        _playerScreenState.value = PlayerScreenState.PlayButtonState(
            drawableRes = pauseDrawableRes,
            isEnabled = true,
            playedTime = interactor.formatPlayedTime()
        )
        handler.postDelayed(updateTimeProgress, UPDATE_TIME_INTERVAL)
    }

    fun pause() {
        if (interactor.currentState == PlayerState.PLAYING) {
            interactor.pause()
            _playerScreenState.value = PlayerScreenState.PlayButtonState(
                drawableRes = playDrawableRes,
                isEnabled = true,
                playedTime = interactor.formatPlayedTime()
            )
            handler.removeCallbacks(updateTimeProgress)
        }
    }

    override fun onCleared() {
        super.onCleared()
        interactor.clearMemory()
    }

    companion object {
        private const val UPDATE_TIME_INTERVAL = 300L
    }
}