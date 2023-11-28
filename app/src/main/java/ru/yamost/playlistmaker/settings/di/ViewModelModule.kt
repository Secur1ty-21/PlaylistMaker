package ru.yamost.playlistmaker.settings.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yamost.playlistmaker.settings.ui.SettingsViewModel

val settingsViewModelModule = module {
    viewModel {
        SettingsViewModel(
            applicationContext = androidContext(),
            sharingInteractor = get(),
            settingsRepository = get()
        )
    }
}