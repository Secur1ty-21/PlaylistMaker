package ru.yamost.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.creator.Creator
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.model.PlayerState
import ru.yamost.playlistmaker.player.ui.model.PlayerScreenState
import ru.yamost.playlistmaker.search.domain.model.Track

class PlayerViewModel(track: Track?) : ViewModel() {
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
        PlayerScreenState.PlayedTime("00:30")
    )
    val playerScreenState: LiveData<PlayerScreenState> get() = _playerScreenState
    private val interactor =
        Creator.providePlayerInteractor(_trackInfoState.value?.previewUrl ?: "")
    private val handler = Handler(Looper.getMainLooper())
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
        preparePlayer()
    }

    private fun preparePlayer() {
        _playerScreenState.value = PlayerScreenState.PlayButtonState(
            drawableRes = playDrawableRes, isEnabled = false
        )
        interactor.prepare(object : PlayerInteractor.PlayerConsumer {
            override fun onReadyForUse() {
                _playerScreenState.value = PlayerScreenState.PlayButtonState(
                    drawableRes = playDrawableRes, isEnabled = true, playedTime = "00:30"
                )
            }

            override fun onTrackEnd() {
                _playerScreenState.value = PlayerScreenState.PlayButtonState(
                    drawableRes = playDrawableRes, isEnabled = true, interactor.formatPlayedTime()
                )
            }

            override fun onError() {
                preparePlayer()
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
        interactor.pause()
        _playerScreenState.value = PlayerScreenState.PlayButtonState(
            drawableRes = playDrawableRes,
            isEnabled = true,
            playedTime = interactor.formatPlayedTime()
        )
        handler.removeCallbacks(updateTimeProgress)
    }

    override fun onCleared() {
        super.onCleared()
        interactor.clearMemory()
    }

    companion object {
        private const val UPDATE_TIME_INTERVAL = 300L

        fun getViewModelFactory(track: Track?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(track)
            }
        }
    }
}