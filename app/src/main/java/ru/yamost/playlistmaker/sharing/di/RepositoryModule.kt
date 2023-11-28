package ru.yamost.playlistmaker.sharing.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.yamost.playlistmaker.sharing.data.SharingStringResRepositoryImpl
import ru.yamost.playlistmaker.sharing.domain.api.SharingStringResRepository

val sharingRepositoryModule = module {
    single<SharingStringResRepository> {
        SharingStringResRepositoryImpl(context = androidContext())
    }
}