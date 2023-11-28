package ru.yamost.playlistmaker.settings.data

import android.content.SharedPreferences
import ru.yamost.playlistmaker.settings.domain.api.SettingsRepository
import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings

class SharedPrefSettingsRepository(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(
            isDarkTheme = sharedPreferences.getBoolean(
                KEY_THEME_SETTINGS, false
            )
        )
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(KEY_THEME_SETTINGS, settings.isDarkTheme).apply()
    }

    override fun isThemeSettingsExist(): Boolean {
        return sharedPreferences.contains(KEY_THEME_SETTINGS)
    }

    companion object {
        private const val KEY_THEME_SETTINGS = "isDarkTheme"
    }
}