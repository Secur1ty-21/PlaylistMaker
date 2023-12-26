package ru.yamost.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.model.PlayerState
import ru.yamost.playlistmaker.player.ui.model.PlayerScreenState
import ru.yamost.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat

class PlayerViewModel(
    private val track: Track?,
    private val interactor: PlayerInteractor,
    private val formatter: SimpleDateFormat
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
        PlayerScreenState.PlayedTime(formatter.format(PlayerInteractor.MAX_AVAILABLE_TRACK_DURATION))
    )
    val playerScreenState: LiveData<PlayerScreenState> get() = _playerScreenState
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite
    private var updateTimeProgressJob: Job? = null

    init {
        track?.let {
            viewModelScope.launch(Dispatchers.IO) {
                if (it.isFavorite) {
                    _isFavorite.postValue(true)
                } else {
                    interactor.isTrackInFavorite(it).collect {
                        _isFavorite.postValue(it)
                    }
                }
            }
        }
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
                    playedTime = formatter.format(PlayerInteractor.MAX_AVAILABLE_TRACK_DURATION),
                )
            }

            override fun onTrackEnd() {
                updateTimeProgressJob?.cancel()
                _playerScreenState.value = PlayerScreenState.PlayButtonState(
                    drawableRes = playDrawableRes,
                    isEnabled = true,
                    playedTime = formatter.format(PlayerInteractor.START_TRACK_TIME)
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
            null
        )
        updateTimeProgressJob = viewModelScope.launch {
            while (interactor.currentState == PlayerState.PLAYING) {
                delay(UPDATE_TIME_INTERVAL)
                _playerScreenState.value = PlayerScreenState.PlayedTime(
                    playedTime = formatter.format(interactor.playedTime())
                )
            }
        }
    }

    fun pause() {
        if (interactor.currentState == PlayerState.PLAYING) {
            interactor.pause()
            updateTimeProgressJob?.cancel()
            _playerScreenState.value = PlayerScreenState.PlayButtonState(
                drawableRes = playDrawableRes,
                isEnabled = true,
                playedTime = formatter.format(interactor.playedTime())
            )
        }
    }

    fun onClickFavoriteBtn() {
        track?.let {
            viewModelScope.launch(Dispatchers.IO) {
                if (isFavorite.value == true) {
                    interactor.deleteTrackFromFavorite(it)
                    _isFavorite.postValue(false)
                } else {
                    interactor.addTrackToFavorite(it)
                    _isFavorite.postValue(true)
                }
            }
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