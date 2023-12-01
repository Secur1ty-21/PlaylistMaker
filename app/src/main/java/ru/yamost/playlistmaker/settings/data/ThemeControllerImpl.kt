package ru.yamost.playlistmaker.settings.data

import android.content.res.Configuration
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

    override fun isSystemInDarkMode(uiMode: Int): Boolean {
        val nightMask = Configuration.UI_MODE_NIGHT_MASK
        return uiMode and nightMask == Configuration.UI_MODE_NIGHT_YES
    }
}