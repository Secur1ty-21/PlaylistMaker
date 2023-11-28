package ru.yamost.playlistmaker.settings.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ru.yamost.playlistmaker.settings.domain.api.SettingsRepository
import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings

class SharedPrefSettingsRepository(
    context: Context
) : SettingsRepository {
    private val sharedPreferences = context.getSharedPreferences(
        SETTINGS_FILE_NAME, MODE_PRIVATE
    )

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
        private const val SETTINGS_FILE_NAME = "Settings"
        private const val KEY_THEME_SETTINGS = "isDarkTheme"
    }
}