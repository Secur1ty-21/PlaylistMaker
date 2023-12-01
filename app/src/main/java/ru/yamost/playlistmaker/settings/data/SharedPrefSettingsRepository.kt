package ru.yamost.playlistmaker.settings.data

import android.content.SharedPreferences
import ru.yamost.playlistmaker.settings.domain.api.SettingsRepository
import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings

class SharedPrefSettingsRepository(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {
    override fun getUserThemeSettings(): ThemeSettings {
        return ThemeSettings(
            isDarkTheme = sharedPreferences.getBoolean(
                KEY_THEME_SETTINGS, false
            )
        )
    }

    override fun saveUserThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(KEY_THEME_SETTINGS, settings.isDarkTheme).apply()
    }

    override fun saveDeviceThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(KEY_SELECTED_DEVICE_THEME, settings.isDarkTheme).apply()
    }

    override fun isUserThemeSettingsExist(): Boolean {
        return sharedPreferences.contains(KEY_THEME_SETTINGS)
    }

    override fun getDeviceThemeSettings(): ThemeSettings {
        return ThemeSettings(
            isDarkTheme = sharedPreferences.getBoolean(
                KEY_SELECTED_DEVICE_THEME,
                false
            )
        )
    }

    override fun isDeviceThemeSettingsExist(): Boolean {
        return sharedPreferences.contains(KEY_SELECTED_DEVICE_THEME)
    }

    companion object {
        private const val KEY_THEME_SETTINGS = "isDarkTheme"
        private const val KEY_SELECTED_DEVICE_THEME = "isDeviceDarkTheme"
    }
}