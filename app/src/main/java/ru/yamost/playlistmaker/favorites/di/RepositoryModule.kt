package ru.yamost.playlistmaker.favorites.di

import org.koin.dsl.module
import ru.yamost.playlistmaker.favorites.data.FavoriteTrackRepositoryImpl
import ru.yamost.playlistmaker.favorites.data.TrackMapper
import ru.yamost.playlistmaker.favorites.domain.api.FavoriteTrackRepository

val favoriteRepositoryModule = module {
    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(
            favoriteTrackDatabase = get(),
            trackMapper = get()
        )
    }

    single {
        TrackMapper()
    }
}