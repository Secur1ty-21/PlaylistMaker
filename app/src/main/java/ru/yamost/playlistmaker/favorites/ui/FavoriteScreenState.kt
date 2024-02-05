package ru.yamost.playlistmaker.favorites.ui

import ru.yamost.playlistmaker.search.domain.model.Track

sealed interface FavoriteScreenState {
    data object Loading : FavoriteScreenState
    data class Content(val trackList: List<Track>) : FavoriteScreenState
    data object Empty : FavoriteScreenState
}