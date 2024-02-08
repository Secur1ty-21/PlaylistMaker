package ru.yamost.playlistmaker.playlist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Date

@Entity(tableName = "playlist_track_xref_table", primaryKeys = ["p_id", "t_id"])
class PlaylistTrackEntity(
    @ColumnInfo(name = "p_id")
    val pId: Int,
    @ColumnInfo(name = "t_id")
    val tId: Int,
    @ColumnInfo(name = "created_date")
    val createdDate: Long = Date().time
)