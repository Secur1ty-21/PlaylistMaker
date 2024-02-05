package ru.yamost.playlistmaker.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.favorites.domain.api.FavoriteTrackInteractor
import ru.yamost.playlistmaker.favorites.domain.api.FavoriteTrackRepository
import ru.yamost.playlistmaker.search.domain.model.Track

class FavoriteTrackInteractorImpl(
    private val favoriteTrackRepository: FavoriteTrackRepository
) : FavoriteTrackInteractor {
    override fun getTrackList(): Flow<List<Track>> {
        return favoriteTrackRepository.getTrackList()
    }
}