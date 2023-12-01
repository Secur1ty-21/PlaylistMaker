package ru.yamost.playlistmaker.settings.domain.impl

import ru.yamost.playlistmaker.settings.domain.api.SettingsInteractor
import ru.yamost.playlistmaker.settings.domain.api.SettingsRepository
import ru.yamost.playlistmaker.settings.domain.api.ThemeController
import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
    private val themeController: ThemeController
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return if (settingsRepository.isUserThemeSettingsExist()) {
            settingsRepository.getUserThemeSettings()
        } else {
            settingsRepository.getDeviceThemeSettings()
        }
    }

    override fun updateUserThemeSettings(themeSettings: ThemeSettings) {
        settingsRepository.saveUserThemeSetting(themeSettings)
        themeController.switchTheme(isDarkTheme = themeSettings.isDarkTheme)
    }

    override fun updateDeviceThemeSettings(uiMode: Int) {
        if (!settingsRepository.isUserThemeSettingsExist()) {
            val isDarkTheme = themeController.isSystemInDarkMode(uiMode)
            themeController.switchTheme(isDarkTheme)
            settingsRepository.saveDeviceThemeSetting(ThemeSettings(isDarkTheme))
        }
    }
}