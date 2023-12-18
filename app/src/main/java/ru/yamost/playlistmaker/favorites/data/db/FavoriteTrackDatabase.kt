package ru.yamost.playlistmaker.favorites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.yamost.playlistmaker.favorites.data.db.dao.FavoriteTrackDao
import ru.yamost.playlistmaker.favorites.data.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class],
    exportSchema = false
)
abstract class FavoriteTrackDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTrackDao
}