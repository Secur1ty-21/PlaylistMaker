package ru.yamost.playlistmaker.playlist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import ru.yamost.playlistmaker.favorites.data.db.entity.TrackEntity

@Entity(tableName = "playlist_track_xref_table", primaryKeys = ["playlist_id", "track_id"])
class PlaylistTrackEntity(
    @ColumnInfo(name = "playlist_id")
    val playlistId: Int,
    @ColumnInfo(name = "track_id")
    val trackId: Int
)