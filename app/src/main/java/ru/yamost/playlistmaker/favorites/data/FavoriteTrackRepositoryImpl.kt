package ru.yamost.playlistmaker.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.yamost.playlistmaker.favorites.data.db.FavoriteTrackDatabase
import ru.yamost.playlistmaker.favorites.data.db.entity.TrackEntity
import ru.yamost.playlistmaker.favorites.domain.api.FavoriteTrackRepository
import ru.yamost.playlistmaker.search.domain.model.Track

class FavoriteTrackRepositoryImpl(
    private val favoriteTrackDatabase: FavoriteTrackDatabase,
    private val trackMapper: TrackMapper
) : FavoriteTrackRepository {
    private val favoriteIdMap = mutableMapOf<Int, Boolean>()

    override fun getTrackList(): Flow<List<Track>> = flow {
        emit(mapToDomain(favoriteTrackDatabase.favoriteTrackDao().getTrackList()))
    }

    override suspend fun getFavoriteIdMap(): Map<Int, Boolean> {
        return favoriteIdMap.ifEmpty {
            for (id in favoriteTrackDatabase.favoriteTrackDao().getTrackIdList()) {
                favoriteIdMap[id] = true
            }
            favoriteIdMap
        }
    }

    private fun mapToDomain(trackList: List<TrackEntity>): List<Track> {
        return trackList.map { trackMapper.mapToDomain(it) }
    }

    override fun isTrackExist(track: Track): Flow<Boolean> = flow {
        if (favoriteIdMap.isEmpty()) {
            emit(favoriteTrackDatabase.favoriteTrackDao().isTrackExist(id = track.id))
        } else {
            emit(favoriteIdMap.contains(track.id))
        }
    }

    override suspend fun saveTrack(track: Track) {
        favoriteIdMap[track.id] = true
        favoriteTrackDatabase.favoriteTrackDao().saveTrack(trackMapper.mapToEntity(track))
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteIdMap.remove(track.id)
        favoriteTrackDatabase.favoriteTrackDao().deleteTrack(trackMapper.mapToEntity(track))
    }
}