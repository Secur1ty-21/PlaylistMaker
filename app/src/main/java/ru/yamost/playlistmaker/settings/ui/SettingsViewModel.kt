package ru.yamost.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.yamost.playlistmaker.App
import ru.yamost.playlistmaker.creator.Creator
import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings

class SettingsViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    private val sharingInteractor = Creator.provideSharingInteractor(application)
    private val settingsRepository = Creator.getSettingsRepository(application.baseContext)
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
        (application as App).switchTheme(isDarkTheme)
        _isDarkTheme.value = isDarkTheme
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}