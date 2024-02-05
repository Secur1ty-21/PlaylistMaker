package ru.yamost.playlistmaker.favorites.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.yamost.playlistmaker.core.data.db.AppDatabase

val favoriteDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "favorite.db")
            .build()
    }
}