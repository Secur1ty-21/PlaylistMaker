package ru.yamost.playlistmaker.player.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.get
import org.koin.dsl.module
import ru.yamost.playlistmaker.player.presentation.PlayerViewModel
import ru.yamost.playlistmaker.search.domain.api.DateTimeRepository
import ru.yamost.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat

val playerViewModelModule = module {
    viewModel { (track: Track?) ->
        PlayerViewModel(
            track = track,
            interactor = get(),
            playlistInteractor = get(),
            formatter = get { parametersOf(get(DateTimeRepository::class.java)) }
        )
    }
    single { (dateTimeRepository: DateTimeRepository) ->
        SimpleDateFormat(
            dateTimeRepository.getTrackTimeFormat(),
            dateTimeRepository.getPreferredLocale()
        )
    }
}