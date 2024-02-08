package ru.yamost.playlistmaker.create.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistInteractor

class CreateViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistId: Int
) : ViewModel() {
    var state: CreateState = CreateState()
        private set
    private var _action = MutableLiveData<CreateAction?>()
    val action: LiveData<CreateAction?> get() = _action

    init {
        if (playlistId != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                playlistInteractor.getPlaylist(playlistId).collect {
                    state = state.copy(
                        albumName = it.name,
                        albumDescription = it.description,
                        photo = it.imageUri,
                        size = it.size
                    )
                    _action.postValue(CreateAction.SetUiWithPlaylist(it))
                }
            }
        }
    }

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
                    if (playlistId != -1) {
                        playlistInteractor.updatePlaylist(
                            playlistId,
                            state.albumName,
                            state.albumDescription,
                            state.photo,
                            state.size ?: 0
                        )
                    } else {
                        playlistInteractor.createPlaylist(
                            name = state.albumName,
                            description = state.albumDescription,
                            uri = state.photo
                        )
                    }
                }.invokeOnCompletion {
                    viewModelScope.launch(Dispatchers.Main) {
                        _action.value =
                            CreateAction.NavigateBackWithResult(albumName = state.albumName)
                    }
                }
            }

            is CreateEvent.OnBackRequested -> {
                if ((state.photo != null || state.albumName.isNotEmpty() || state.albumDescription.isNotEmpty())
                    && playlistId == -1
                ) {
                    _action.value = CreateAction.ShowAcceptedDialog(
                        onPosBtnClick = { _action.value = null },
                        onCancelBtnClick = { _action.value = null }
                    )
                } else {
                    _action.value = CreateAction.NavigateBack
                }
            }
        }
    }
}