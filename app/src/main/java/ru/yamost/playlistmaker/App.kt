package ru.yamost.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        var isDarkTheme: Boolean = false
        private const val SETTINGS_FILE_NAME = "Settings"
        private const val KEY_DARK_THEME = "isDarkTheme"
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(SETTINGS_FILE_NAME, MODE_PRIVATE)
        if (sharedPreferences.contains(KEY_DARK_THEME)) {
            isDarkTheme = sharedPreferences.getBoolean(KEY_DARK_THEME, false)
            switchTheme(isDarkTheme)
        } else {
            if (isSystemInNightMode()) {
                isDarkTheme = true
            }
            savePreference()
        }
    }

    private fun isSystemInNightMode(): Boolean {
        val currentUiMode = resources.configuration.uiMode
        val nightMask = Configuration.UI_MODE_NIGHT_MASK
        return currentUiMode and nightMask == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        sharedPreferences.edit().putBoolean(KEY_DARK_THEME, isDarkTheme).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun savePreference() {
        sharedPreferences.edit().putBoolean(KEY_DARK_THEME, isDarkTheme).apply()
    }
}