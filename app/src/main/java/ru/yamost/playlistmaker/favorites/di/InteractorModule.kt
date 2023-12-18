package ru.yamost.playlistmaker.favorites.di

import org.koin.dsl.module
import ru.yamost.playlistmaker.favorites.domain.api.FavoriteTrackInteractor
import ru.yamost.playlistmaker.favorites.domain.impl.FavoriteTrackInteractorImpl

val favoriteInteractorModule = module {
    single<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(
            favoriteTrackRepository = get()
        )
    }
}