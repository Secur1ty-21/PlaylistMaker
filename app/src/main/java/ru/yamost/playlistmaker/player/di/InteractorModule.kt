package ru.yamost.playlistmaker.player.di

import org.koin.core.parameter.parametersOf
import org.koin.core.scope.get
import org.koin.dsl.module
import ru.yamost.playlistmaker.player.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.player.domain.impl.PlayerInteractorImpl
import ru.yamost.playlistmaker.search.domain.api.DateTimeRepository
import java.text.SimpleDateFormat

val playerInteractorModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(
            formatter = get { parametersOf(get(DateTimeRepository::class.java)) },
            playerController = get()
        )
    }
    single { (dateTimeRepository: DateTimeRepository) ->
        SimpleDateFormat(
            dateTimeRepository.getTrackTimeFormat(),
            dateTimeRepository.getPreferredLocale()
        )
    }
}