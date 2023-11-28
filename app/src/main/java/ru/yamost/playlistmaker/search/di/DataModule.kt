package ru.yamost.playlistmaker.search.di

import org.koin.dsl.module
import ru.yamost.playlistmaker.search.data.NetworkClient
import ru.yamost.playlistmaker.search.data.network.RetrofitApiProvider
import ru.yamost.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.yamost.playlistmaker.search.data.network.TracksService

val searchDataModule = module {
    single<TracksService> {
        RetrofitApiProvider.tracksService
    }
    single<NetworkClient> {
        RetrofitNetworkClient(tracksService = get())
    }
}