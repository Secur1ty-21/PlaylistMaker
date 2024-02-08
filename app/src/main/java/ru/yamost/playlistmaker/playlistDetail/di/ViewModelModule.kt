package ru.yamost.playlistmaker.playlistDetail.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yamost.playlistmaker.playlistDetail.presentation.PlaylistDetailViewModel

val playlistDetailViewModelModule = module {
    viewModel { (playlistId: Int) ->
        PlaylistDetailViewModel(
            playlistInteractor = get(),
            sharingInteractor = get(),
            playlistId = playlistId
        )
    }
}