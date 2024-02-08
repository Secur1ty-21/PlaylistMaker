package ru.yamost.playlistmaker.playlistDetail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.yamost.playlistmaker.playlist.domain.model.PlaylistWithTracks
import ru.yamost.playlistmaker.playlistDetail.presentation.model.PlaylistDetailAction
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.sharing.domain.api.SharingInteractor

class PlaylistDetailViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
    private val playlistId: Int
) : ViewModel() {
    private val _state = MutableLiveData<PlaylistWithTracks>()
    val state: LiveData<PlaylistWithTracks> get() = _state
    private val _action = MutableLiveData<PlaylistDetailAction?>(null)
    val action: LiveData<PlaylistDetailAction?> get() = _action
    private val _isMoreBottomSheetVisible = MutableLiveData<Boolean>()
    val isMoreBottomSheetVisible: LiveData<Boolean> get() = _isMoreBottomSheetVisible

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistWithTracks(playlistId = playlistId).collect {
                if (it.isSuccess) {
                    _state.postValue(it.getOrThrow())
                }
            }
        }
    }

    fun onDialogBtnDeleteTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deleteTrackFromPlaylist(
                playlistId = playlistId,
                trackId = track.id,
                state.value?.playlist?.size ?: 1
            )
            playlistInteractor.getPlaylistWithTracks(playlistId = playlistId).collect {
                if (it.isSuccess) {
                    _state.postValue(it.getOrThrow())
                }
            }
        }
    }

    fun onBtnDeletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value?.let {
                playlistInteractor.deletePlaylist(it)
            }
        }.invokeOnCompletion {
            viewModelScope.launch {
                _action.postValue(PlaylistDetailAction.NavigateBack)
            }
        }
    }

    fun onBtnShareClick() {
        state.value?.let {
            if (it.tracks.isEmpty()) {
                viewModelScope.launch {
                    _action.postValue(PlaylistDetailAction.ShowEmptyPlaylistMessage)
                    delay(500L)
                    _action.postValue(null)
                }
            } else {
                sharingInteractor.sharePlaylist(it)
            }
        }
    }

    fun onBtnEditClick() {
        viewModelScope.launch {
            _action.postValue(PlaylistDetailAction.NavigateToEditScreen(playlistId))
            delay(500L)
            _action.postValue(null)
        }
    }

    fun updatePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylist(playlistId = playlistId).collect {
                _action.postValue(PlaylistDetailAction.UpdatePlaylist(it))
                delay(500L)
                _action.postValue(null)
            }
        }
    }

    fun onBtnMoreClick() {
        _isMoreBottomSheetVisible.postValue(true)
    }

    fun onCloseMoreBottomSheet() {
        _isMoreBottomSheetVisible.postValue(false)
    }
}