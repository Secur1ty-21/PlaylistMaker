package ru.yamost.playlistmaker.creator

import android.content.Context
import ru.yamost.playlistmaker.player.data.PlayerControllerImpl
import ru.yamost.playlistmaker.player.domain.api.PlayerController
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.impl.PlayerInteractorImpl
import ru.yamost.playlistmaker.search.data.DateTimeRepositoryImpl
import ru.yamost.playlistmaker.search.data.TrackRepositoryImpl
import ru.yamost.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.yamost.playlistmaker.search.data.storage.SharedPrefSearchHistoryRepository
import ru.yamost.playlistmaker.search.domain.api.DateTimeRepository
import ru.yamost.playlistmaker.search.domain.api.SearchHistoryInteractor
import ru.yamost.playlistmaker.search.domain.api.SearchHistoryRepository
import ru.yamost.playlistmaker.search.domain.api.TrackRepository
import ru.yamost.playlistmaker.search.domain.api.TracksInteractor
import ru.yamost.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import ru.yamost.playlistmaker.search.domain.impl.TrackInteractorImpl
import ru.yamost.playlistmaker.settings.data.SharedPrefSettingsRepository
import ru.yamost.playlistmaker.settings.domain.api.SettingsRepository
import ru.yamost.playlistmaker.sharing.data.ExternalNavigatorImpl
import ru.yamost.playlistmaker.sharing.data.SharingStringResRepositoryImpl
import ru.yamost.playlistmaker.sharing.domain.api.ExternalNavigator
import ru.yamost.playlistmaker.sharing.domain.api.SharingInteractor
import ru.yamost.playlistmaker.sharing.domain.api.SharingStringResRepository
import ru.yamost.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private fun getDateTimeRepository(): DateTimeRepository {
        return DateTimeRepositoryImpl()
    }

    private fun getPlayerController(trackUrl: String): PlayerController {
        return PlayerControllerImpl(trackUrl = trackUrl)
    }

    fun providePlayerInteractor(trackUrl: String): PlayerInteractor {
        return PlayerInteractorImpl(
            getDateTimeRepository(), getPlayerController(trackUrl = trackUrl)
        )
    }

    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(
            getDateTimeRepository(), RetrofitNetworkClient()
        )
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TrackInteractorImpl(
            getTracksRepository()
        )
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SharedPrefSearchHistoryRepository(context)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(
            getSearchHistoryRepository(context)
        )
    }

    fun getSettingsRepository(context: Context): SettingsRepository {
        return SharedPrefSettingsRepository(context)
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    private fun getSharingStringResRepository(context: Context): SharingStringResRepository {
        return SharingStringResRepositoryImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            getExternalNavigator(context), getSharingStringResRepository(context)
        )
    }
}