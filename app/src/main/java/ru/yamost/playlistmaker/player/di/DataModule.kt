package ru.yamost.playlistmaker.player.di

import android.media.MediaPlayer
import org.koin.dsl.module
import ru.yamost.playlistmaker.player.data.PlayerControllerImpl
import ru.yamost.playlistmaker.player.domain.api.PlayerController

val playerDataModule = module {
    factory<PlayerController> {
        PlayerControllerImpl(mediaPlayer = get())
    }
    factory {
        MediaPlayer()
    }
}