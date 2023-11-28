package ru.yamost.playlistmaker.settings.di

import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import ru.yamost.playlistmaker.settings.data.SharedPrefSettingsRepository
import ru.yamost.playlistmaker.settings.domain.api.SettingsRepository

val settingsRepositoryModule = module {
    single<SettingsRepository> {
        SharedPrefSettingsRepository(
            sharedPreferences = get { parametersOf("Settings") }
        )
    }
}