package ru.yamost.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.yamost.playlistmaker.settings.domain.api.SettingsRepository
import ru.yamost.playlistmaker.settings.domain.model.ThemeSettings

class App : Application() {
    private val settingsRepository: SettingsRepository by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(DiModuleProvider.searchModules)
            modules(DiModuleProvider.playerModules)
            modules(DiModuleProvider.settingsModules)
            modules(DiModuleProvider.sharingModules)
        }
        if (settingsRepository.isThemeSettingsExist()) {
            switchTheme(settingsRepository.getThemeSettings().isDarkTheme)
        } else {
            settingsRepository.updateThemeSetting(ThemeSettings(isSystemInNightMode()))
        }
    }

    private fun isSystemInNightMode(): Boolean {
        val currentUiMode = resources.configuration.uiMode
        val nightMask = Configuration.UI_MODE_NIGHT_MASK
        return currentUiMode and nightMask == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}