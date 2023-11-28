package ru.yamost.playlistmaker.settings.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.yamost.playlistmaker.App
import ru.yamost.playlistmaker.settings.domain.api.SettingsRepository
import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings
import ru.yamost.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val applicationContext: Context,
    private val sharingInteractor: SharingInteractor,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _isDarkTheme = MutableLiveData(settingsRepository.getThemeSettings().isDarkTheme)
    val isDarkTheme: LiveData<Boolean>
        get() = _isDarkTheme

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun switchTheme(isDarkTheme: Boolean) {
        settingsRepository.updateThemeSetting(
            ThemeSettings(isDarkTheme)
        )
        (applicationContext as App).switchTheme(isDarkTheme)
        _isDarkTheme.value = isDarkTheme
    }
}