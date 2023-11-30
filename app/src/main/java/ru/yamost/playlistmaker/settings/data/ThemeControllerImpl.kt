package ru.yamost.playlistmaker.settings.data

import androidx.appcompat.app.AppCompatDelegate
import ru.yamost.playlistmaker.settings.domain.api.ThemeController

class ThemeControllerImpl : ThemeController {
    override fun switchTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}