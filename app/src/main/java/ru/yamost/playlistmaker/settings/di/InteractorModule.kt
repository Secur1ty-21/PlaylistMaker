package ru.yamost.playlistmaker.settings.di

import org.koin.dsl.module
import ru.yamost.playlistmaker.settings.data.ThemeControllerImpl
import ru.yamost.playlistmaker.settings.domain.api.SettingsInteractor
import ru.yamost.playlistmaker.settings.domain.api.ThemeController
import ru.yamost.playlistmaker.settings.domain.impl.SettingsInteractorImpl

val settingsInteractorModule = module {
    single<SettingsInteractor> {
        SettingsInteractorImpl(
            settingsRepository = get(),
            themeController = get()
        )
    }
    single<ThemeController> {
        ThemeControllerImpl()
    }
}