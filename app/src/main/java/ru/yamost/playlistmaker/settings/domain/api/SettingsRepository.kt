package ru.yamost.playlistmaker.settings.domain.api

import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
    fun isThemeSettingsExist(): Boolean
}