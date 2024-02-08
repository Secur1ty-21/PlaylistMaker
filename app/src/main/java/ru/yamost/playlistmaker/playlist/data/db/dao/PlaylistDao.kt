package ru.yamost.playlistmaker.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistEntity
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistTrackEntity
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistWithTracksDto
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

    @Query("SELECT EXISTS(SELECT * FROM playlist_track_xref_table WHERE (p_id = :playlistId AND  t_id = :trackId))")
    fun isTrackExistInPlaylist(playlistId: Int, trackId: Int): Boolean

    @Query("UPDATE playlist_table SET size = :newSize WHERE playlist_id = :playlistId")
    fun updatePlaylistSize(newSize: Int, playlistId: Int)

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlist_id = :playlistId")
    fun getPlaylistWithTracks(playlistId: Int): Flow<PlaylistWithTracksDto?>

    @Query("SELECT * FROM playlist_track_xref_table WHERE p_id = :playlistId")
    fun getPlaylistFromPlaylistTrackXrefTable(playlistId: Int): List<PlaylistTrackEntity>

    @Query("SELECT * FROM playlist_table WHERE playlist_id = :id")
    fun getPlaylist(id: Int): Flow<PlaylistEntity>

    @Transaction
    fun deleteTrackFromPlaylist(playlistTrackEntity: PlaylistTrackEntity, playlistSize: Int) {
        deleteTrackFromPlaylistTrackXrefTable(playlistTrackEntity)
        updatePlaylistSize(newSize = playlistSize - 1, playlistId = playlistTrackEntity.pId)
        if (!isTrackExistInPlaylistTrackXrefTable(playlistTrackEntity.tId)) {
            deleteTrackFromTrackTable(trackId = playlistTrackEntity.tId)
        }
    }

    @Delete(PlaylistTrackEntity::class)
    fun deleteTrackFromPlaylistTrackXrefTable(playlistTrackEntity: PlaylistTrackEntity)

    @Query("SELECT EXISTS(SELECT * FROM playlist_track_xref_table WHERE t_id = :trackId)")
    fun isTrackExistInPlaylistTrackXrefTable(trackId: Int): Boolean

    @Query("DELETE from track_table WHERE track_id = :trackId")
    fun deleteTrackFromTrackTable(trackId: Int)

    @Query("DELETE from playlist_table WHERE playlist_id = :playlistId")
    fun deletePlaylist(playlistId: Int)

    @Transaction
    fun deletePlaylistWithTracks(playlistId: Int, tracksId: List<Int>) {
        for (id in tracksId) {
            val playlistTrackEntity = PlaylistTrackEntity(
                pId = playlistId,
                tId = id
            )
            deleteTrackFromPlaylistTrackXrefTable(playlistTrackEntity)
            if (!isTrackExistInPlaylistTrackXrefTable(playlistTrackEntity.tId)) {
                deleteTrackFromTrackTable(trackId = playlistTrackEntity.tId)
            }
        }
        deletePlaylist(playlistId = playlistId)
    }

    @Update(PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlistEntity: PlaylistEntity)
}