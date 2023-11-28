package ru.yamost.playlistmaker.sharing.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.yamost.playlistmaker.sharing.data.ExternalNavigatorImpl
import ru.yamost.playlistmaker.sharing.domain.api.ExternalNavigator

val sharingDataModule = module {
    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext())
    }
}