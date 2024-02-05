package ru.yamost.playlistmaker.create.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistInteractor

class CreateViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    var state: CreateState = CreateState()
        private set
    private var action = MutableLiveData<CreateAction?>()
    fun observeAction(): LiveData<CreateAction?> = action

    fun obtainEvent(viewEvent: CreateEvent) {
        when (viewEvent) {
            is CreateEvent.OnAlbumNameType -> {
                state = state.copy(albumName = viewEvent.albumName)
            }

            is CreateEvent.OnAlbumDescriptionType -> {
                state = state.copy(albumDescription = viewEvent.albumDescription)
            }

            is CreateEvent.OnPhotoCaptured -> {
                state = state.copy(photo = viewEvent.bitmap)
            }

            is CreateEvent.OnBtnCreateClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    playlistInteractor.createPlaylist(
                        name = state.albumName,
                        description = state.albumDescription,
                        uri = state.photo
                    )
                }.invokeOnCompletion {
                    viewModelScope.launch(Dispatchers.Main) {
                        action.value = CreateAction.NavigateBackWithResult(albumName = state.albumName)
                    }
                }
            }

            is CreateEvent.OnBackRequested -> {
                if (state.photo != null || state.albumName.isNotEmpty() || state.albumDescription.isNotEmpty()) {
                    action.value = CreateAction.ShowAcceptedDialog(
                        onPosBtnClick = { action.value = null },
                        onCancelBtnClick = { action.value = null }
                    )
                } else {
                    action.value = CreateAction.NavigateBack
                }
            }
        }
    }
}