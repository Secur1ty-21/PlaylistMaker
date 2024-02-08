package ru.yamost.playlistmaker.player.presentation

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
import ru.yamost.playlistmaker.player.presentation.model.PlayerAction
import ru.yamost.playlistmaker.player.presentation.model.PlayerScreenState
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.yamost.playlistmaker.playlist.domain.model.Playlist
import ru.yamost.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat

class PlayerViewModel(
    private val track: Track?,
    private val interactor: PlayerInteractor,
    private val playlistInteractor: PlaylistInteractor,
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
    private val playlistListState = MutableLiveData<List<Playlist>>()
    val action = MutableLiveData<PlayerAction?>()
    private var updateTimeProgressJob: Job? = null

    fun observePlaylistListState(): LiveData<List<Playlist>> = playlistListState
    fun observeAction(): LiveData<PlayerAction?> = action

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
        updatePlaylistList()
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

    fun updatePlaylistList() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistList().collect {
                playlistListState.postValue(it)
            }
        }
    }

    fun onPlaylistItemClickEvent(playlist: Playlist) {
        if (track != null) {
            viewModelScope.launch(Dispatchers.IO) {
                if (playlistInteractor.isTrackInPlaylist(track, playlist)) {
                    action.postValue(PlayerAction.ShowSnackbar(isTrackAdded = false, playlist.name))
                    delay(CLEAR_ACTION_TIME_INTERVAL_MILLIS)
                    action.postValue(null)
                } else {
                    playlistInteractor.addTrackToPlaylist(track, playlist)
                    updatePlaylistList()
                    action.postValue(PlayerAction.ShowSnackbar(isTrackAdded = true, playlist.name))
                    delay(CLEAR_ACTION_TIME_INTERVAL_MILLIS)
                    action.postValue(null)
                }
            }
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
                delay(UPDATE_TIME_INTERVAL_MILLIS)
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
        private const val UPDATE_TIME_INTERVAL_MILLIS = 300L
        private const val CLEAR_ACTION_TIME_INTERVAL_MILLIS = 2000L
    }
}