package ru.yamost.playlistmaker.favorites.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorite_track_table")
data class FavoriteTrackEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
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