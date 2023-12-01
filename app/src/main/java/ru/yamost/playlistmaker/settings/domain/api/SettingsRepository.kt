package ru.yamost.playlistmaker.settings.domain.api

import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getUserThemeSettings(): ThemeSettings
    fun getDeviceThemeSettings(): ThemeSettings
    fun saveUserThemeSetting(settings: ThemeSettings)
    fun isUserThemeSettingsExist(): Boolean
    fun isDeviceThemeSettingsExist(): Boolean
    fun saveDeviceThemeSetting(settings: ThemeSettings)
}