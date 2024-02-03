package ru.yamost.playlistmaker.create.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yamost.playlistmaker.create.presentation.CreateViewModel

val createViewModelModule = module {
    viewModel {
        CreateViewModel(
            playlistInteractor = get()
        )
    }
}