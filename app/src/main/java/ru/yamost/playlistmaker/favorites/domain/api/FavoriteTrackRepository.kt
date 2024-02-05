package ru.yamost.playlistmaker.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.search.domain.model.Track

interface FavoriteTrackRepository {
    suspend fun getFavoriteIdMap(): Map<Int, Boolean>
    fun getTrackList(): Flow<List<Track>>
    fun isTrackExist(track: Track): Flow<Boolean>
    suspend fun saveTrack(track: Track)
    suspend fun deleteTrack(track: Track)
}