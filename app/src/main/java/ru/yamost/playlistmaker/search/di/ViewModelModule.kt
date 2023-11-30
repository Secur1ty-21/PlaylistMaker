package ru.yamost.playlistmaker.search.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yamost.playlistmaker.search.ui.SearchViewModel

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(
            historyInteractor = get(),
            searchInteractor = get(),
            handler = get()
        )
    }
}