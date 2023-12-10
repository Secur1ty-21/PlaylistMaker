package ru.yamost.playlistmaker.search.di

import org.koin.dsl.module
import ru.yamost.playlistmaker.search.domain.api.SearchHistoryInteractor
import ru.yamost.playlistmaker.search.domain.api.TracksInteractor
import ru.yamost.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import ru.yamost.playlistmaker.search.domain.impl.TrackInteractorImpl
import java.util.concurrent.Executors

val searchInteractorModule = module {
    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(repository = get())
    }
    single<TracksInteractor> {
        TrackInteractorImpl(
            repository = get()
        )
    }
    factory {
        Executors.newCachedThreadPool()
    }
}