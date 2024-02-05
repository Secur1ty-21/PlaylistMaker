package ru.yamost.playlistmaker.player.di

import org.koin.dsl.module
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.impl.PlayerInteractorImpl

val playerInteractorModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(
            playerController = get()
        )
    }
}