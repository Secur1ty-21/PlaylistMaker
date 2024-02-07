package ru.yamost.playlistmaker.playlist.di

import org.koin.dsl.module
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.yamost.playlistmaker.playlist.domain.impl.PlaylistInteractorImpl

val playlistInteractorModule = module {
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(
            playlistRepository = get()
        )
    }
}