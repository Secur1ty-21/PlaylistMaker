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
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSettings(themeSettings: ThemeSettings) {
        settingsRepository.updateThemeSetting(themeSettings)
        themeController.switchTheme(isDarkTheme = themeSettings.isDarkTheme)
    }
}