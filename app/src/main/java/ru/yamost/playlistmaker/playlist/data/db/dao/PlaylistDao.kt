package ru.yamost.playlistmaker.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistEntity
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistTrackEntity
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistWithTracks

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun createPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table ORDER BY created_date DESC")
    fun getPlaylistList(): Flow<List<PlaylistEntity>>

    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun saveTrackToPlaylist(playlistTrackEntity: PlaylistTrackEntity)

    @Query("UPDATE playlist_table SET size = :newSize WHERE id = :playlistId")
    fun updatePlaylistSize(newSize: Int, playlistId: Int)

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylist(playlistId: Int): PlaylistWithTracks
}