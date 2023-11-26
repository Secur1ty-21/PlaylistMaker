package ru.yamost.playlistmaker

import android.content.SharedPreferences
import ru.yamost.playlistmaker.data.DateTimeRepositoryImpl
import ru.yamost.playlistmaker.data.TrackRepositoryImpl
import ru.yamost.playlistmaker.data.cache.SharedPrefSearchHistoryRepository
import ru.yamost.playlistmaker.data.network.PlayerControllerImpl
import ru.yamost.playlistmaker.data.network.RetrofitNetworkClient
import ru.yamost.playlistmaker.domain.api.DateTimeRepository
import ru.yamost.playlistmaker.domain.api.PlayerController
import ru.yamost.playlistmaker.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.domain.api.SearchHistoryInteractor
import ru.yamost.playlistmaker.domain.api.SearchHistoryRepository
import ru.yamost.playlistmaker.domain.api.TrackRepository
import ru.yamost.playlistmaker.domain.api.TracksInteractor
import ru.yamost.playlistmaker.domain.impl.PlayerInteractorImpl
import ru.yamost.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import ru.yamost.playlistmaker.domain.impl.TrackInteractorImpl

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

    private fun getSearchHistoryRepository(sharedPreferences: SharedPreferences): SearchHistoryRepository {
        return SharedPrefSearchHistoryRepository(sharedPreferences)
    }

    fun provideSearchHistoryInteractor(sharedPreferences: SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(
            getSearchHistoryRepository(sharedPreferences)
        )
    }
}