package ru.yamost.playlistmaker.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.yamost.playlistmaker.favorites.data.db.dao.FavoriteTrackDao
import ru.yamost.playlistmaker.favorites.data.db.entity.TrackEntity
import ru.yamost.playlistmaker.playlist.data.db.dao.PlaylistDao
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistEntity
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistTrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTrackDao
    abstract fun playlistDao(): PlaylistDao
}