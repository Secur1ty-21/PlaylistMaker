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
    override fun getTrackList(): Flow<List<Track>> = flow {
        emit(mapToDomain(favoriteTrackDatabase.favoriteTrackDao().getTrackList()))
    }

    private fun mapToDomain(trackList: List<TrackEntity>): List<Track> {
        return trackList.map { trackMapper.mapToDomain(it) }
    }

    override fun isTrackExist(track: Track): Flow<Boolean> = flow {
        emit(favoriteTrackDatabase.favoriteTrackDao().isTrackExist(id = track.id))
    }

    override suspend fun saveTrack(track: Track) {
        favoriteTrackDatabase.favoriteTrackDao().saveTrack(trackMapper.mapToEntity(track))
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteTrackDatabase.favoriteTrackDao().deleteTrack(trackMapper.mapToEntity(track))
    }
}