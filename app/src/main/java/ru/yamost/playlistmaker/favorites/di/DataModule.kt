package ru.yamost.playlistmaker.favorites.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.yamost.playlistmaker.favorites.data.db.FavoriteTrackDatabase

val favoriteDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), FavoriteTrackDatabase::class.java, "favorite.db")
            .build()
    }
}