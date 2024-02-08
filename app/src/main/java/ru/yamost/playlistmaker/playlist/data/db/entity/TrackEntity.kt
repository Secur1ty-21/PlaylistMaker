package ru.yamost.playlistmaker.playlist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "track_id")
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val createdDate: Long = Date().time
)