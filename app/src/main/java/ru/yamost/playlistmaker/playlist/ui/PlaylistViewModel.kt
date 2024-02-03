package ru.yamost.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.yamost.playlistmaker.playlist.ui.model.PlaylistScreenState

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val state = MutableLiveData<PlaylistScreenState>()
    fun observeState(): LiveData<PlaylistScreenState> = state

    fun updatePlaylistList() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistList().collect {
                if (it.isNotEmpty()) {
                    state.postValue(PlaylistScreenState.Content(it))
                } else {
                    state.postValue(PlaylistScreenState.Empty)
                }
            }
        }
    }
}