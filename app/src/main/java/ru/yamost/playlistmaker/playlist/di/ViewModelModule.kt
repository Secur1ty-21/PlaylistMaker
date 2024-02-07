package ru.yamost.playlistmaker.playlist.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yamost.playlistmaker.playlist.ui.PlaylistViewModel

val playlistViewModelModule = module {
    viewModel {
        PlaylistViewModel(
            playlistInteractor = get()
        )
    }
}