package ru.yamost.playlistmaker.sharing.di

import org.koin.dsl.module
import ru.yamost.playlistmaker.sharing.domain.api.SharingInteractor
import ru.yamost.playlistmaker.sharing.domain.impl.SharingInteractorImpl

val sharingInteractorModule = module {
    single<SharingInteractor> {
        SharingInteractorImpl(
            externalNavigator = get(),
            stringResRepository = get()
        )
    }
}