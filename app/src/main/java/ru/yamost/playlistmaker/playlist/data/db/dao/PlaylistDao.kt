package ru.yamost.playlistmaker.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistEntity
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistTrackEntity
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistWithTracks
import ru.yamost.playlistmaker.playlist.data.db.entity.TrackEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun createPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table ORDER BY created_date DESC")
    fun getPlaylistList(): Flow<List<PlaylistEntity>>

    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun saveTrackToPlaylist(playlistTrackEntity: PlaylistTrackEntity)

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun saveTrack(trackEntity: TrackEntity)

    @Transaction
    fun addTrackToPlaylist(playlistTrackEntity: PlaylistTrackEntity, trackEntity: TrackEntity) {
        saveTrack(trackEntity)
        saveTrackToPlaylist(playlistTrackEntity)
    }

    @Query("SELECT EXISTS(SELECT * FROM playlist_track_xref_table WHERE (playlist_id = :playlistId AND  track_id = :trackId))")
    fun isTrackExistInPlaylist(playlistId: Int, trackId: Int): Boolean

    @Query("UPDATE playlist_table SET size = :newSize WHERE id = :playlistId")
    fun updatePlaylistSize(newSize: Int, playlistId: Int)

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylist(playlistId: Int): PlaylistWithTracks
}