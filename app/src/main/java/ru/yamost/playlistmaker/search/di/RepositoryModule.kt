package ru.yamost.playlistmaker.search.di

import android.content.Context
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.get
import org.koin.dsl.module
import ru.yamost.playlistmaker.search.data.TrackDateTimeRepositoryImpl
import ru.yamost.playlistmaker.search.data.TrackRepositoryImpl
import ru.yamost.playlistmaker.search.data.SharedPrefSearchHistoryRepository
import ru.yamost.playlistmaker.search.data.mapper.TrackStorageMapper
import ru.yamost.playlistmaker.search.data.dto.TrackStorageDto
import ru.yamost.playlistmaker.search.domain.api.DateTimeRepository
import ru.yamost.playlistmaker.search.domain.api.SearchHistoryRepository
import ru.yamost.playlistmaker.search.domain.api.TrackRepository
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Mapper

val searchRepositoryModule = module {
    single<DateTimeRepository> {
        TrackDateTimeRepositoryImpl()
    }
    single<SearchHistoryRepository> {
        SharedPrefSearchHistoryRepository(
            mapper = get(),
            sharedPreferences = get { parametersOf("Search history") },
            gson = get()
        )
    }
    single<Mapper<List<Track>, List<TrackStorageDto>>> {
        TrackStorageMapper()
    }
    factory { (fileName: String) ->
        androidContext().getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }
    factory {
        Gson()
    }
    single<TrackRepository> {
        TrackRepositoryImpl(
            formatter = get { parametersOf(get(DateTimeRepository::class.java)) },
            networkClient = get(),
            favoriteTrackRepository = get()
        )
    }
}