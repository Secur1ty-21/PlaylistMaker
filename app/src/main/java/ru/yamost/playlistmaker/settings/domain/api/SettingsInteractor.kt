package ru.yamost.playlistmaker.settings.domain.api

import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSettings(themeSettings: ThemeSettings)
}