package ru.yamost.playlistmaker.favorites.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.yamost.playlistmaker.favorites.data.db.entity.TrackEntity

@Dao
interface FavoriteTrackDao {
    @Query("SELECT * FROM favorite_track_table ORDER BY createdDate DESC")
    fun getTrackList(): List<TrackEntity>

    @Query("SELECT id FROM favorite_track_table")
    fun getTrackIdList(): List<Int>

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveTrack(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorite_track_table WHERE id = :id)")
    fun isTrackExist(id: Int): Boolean
}