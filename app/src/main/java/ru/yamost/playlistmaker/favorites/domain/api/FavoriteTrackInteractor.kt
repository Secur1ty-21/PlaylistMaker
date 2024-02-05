package ru.yamost.playlistmaker.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.search.domain.model.Track

interface FavoriteTrackInteractor {
    fun getTrackList(): Flow<List<Track>>
}