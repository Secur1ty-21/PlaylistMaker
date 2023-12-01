package ru.yamost.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.yamost.playlistmaker.settings.domain.api.SettingsInteractor
import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings
import ru.yamost.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    private val _isDarkTheme = MutableLiveData(settingsInteractor.getThemeSettings().isDarkTheme)
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
        settingsInteractor.updateUserThemeSettings(ThemeSettings(isDarkTheme))
        _isDarkTheme.value = isDarkTheme
    }

    fun onSystemUiModeChangedEvent(uiMode: Int) {
        settingsInteractor.updateDeviceThemeSettings(uiMode)
        _isDarkTheme.value = settingsInteractor.getThemeSettings().isDarkTheme
    }
}