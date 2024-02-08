package ru.yamost.playlistmaker.playlist.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import ru.yamost.playlistmaker.core.data.db.AppDatabase
import ru.yamost.playlistmaker.playlist.data.PlaylistMapper
import ru.yamost.playlistmaker.playlist.data.PlaylistRepositoryImpl
import ru.yamost.playlistmaker.playlist.data.TrackMapper
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistRepository
import ru.yamost.playlistmaker.search.data.TrackDateTimeRepositoryImpl
import java.io.File

val playlistRepositoryModule = module {
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            playlistDao = get<AppDatabase>().playlistDao(),
            playlistMapper = get(),
            trackMapper = get(),
            internalDir = File(androidContext().filesDir, "playlistCovers"),
            contentResolver = androidContext().contentResolver,
            trackSdf = get { parametersOf(get(TrackDateTimeRepositoryImpl::class)) }
        )
    }

    single {
        PlaylistMapper()
    }

    single {
        TrackMapper()
    }
}