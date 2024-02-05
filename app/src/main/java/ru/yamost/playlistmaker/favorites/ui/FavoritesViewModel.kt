package ru.yamost.playlistmaker.favorites.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yamost.playlistmaker.favorites.domain.api.FavoriteTrackInteractor

class FavoritesViewModel(
    private val favoriteTrackInteractor: FavoriteTrackInteractor
) : ViewModel() {
    private val state = MutableLiveData<FavoriteScreenState>()
    private val isClickDebounced = MutableLiveData(true)

    fun observeScreenState(): LiveData<FavoriteScreenState> = state
    fun observeIsClickDebounce(): LiveData<Boolean> = isClickDebounced

    init {
        updateFavoriteTracks()
    }

    fun updateFavoriteTracks() {
        viewModelScope.launch(context = Dispatchers.IO) {
            favoriteTrackInteractor.getTrackList().collect {
                if (it.isNotEmpty()) {
                    state.postValue(FavoriteScreenState.Content(it))
                } else {
                    state.postValue(FavoriteScreenState.Empty)
                }
            }
        }
    }

    fun debounceClick() {
        viewModelScope.launch {
            delay(ITEM_CLICKED_DEBOUNCE_DELAY)
            isClickDebounced.value = true
        }
    }

    companion object {
        private const val ITEM_CLICKED_DEBOUNCE_DELAY = 1000L
    }
}