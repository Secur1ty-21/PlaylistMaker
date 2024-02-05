package ru.yamost.playlistmaker.favorites.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yamost.playlistmaker.favorites.ui.FavoritesViewModel

val favoritesViewModelModule = module {
    viewModel {
        FavoritesViewModel(
            favoriteTrackInteractor = get()
        )
    }
}