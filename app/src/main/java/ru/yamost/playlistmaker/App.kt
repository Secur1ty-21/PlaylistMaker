package ru.yamost.playlistmaker

import android.app.Application
import android.content.res.Configuration
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.yamost.playlistmaker.settings.domain.api.SettingsRepository
import ru.yamost.playlistmaker.settings.domain.api.ThemeController

class App : Application() {
    private val settingsRepository: SettingsRepository by inject()
    private val themeController: ThemeController by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(DiModuleProvider.searchModules)
            modules(DiModuleProvider.playerModules)
            modules(DiModuleProvider.settingsModules)
            modules(DiModuleProvider.sharingModules)
            modules(DiModuleProvider.favoritesModules)
            modules(DiModuleProvider.playlistModules)
        }
        if (settingsRepository.isThemeSettingsExist()) {
            themeController.switchTheme(settingsRepository.getThemeSettings().isDarkTheme)
        } else {
            themeController.switchTheme(isSystemInNightMode())
        }
    }

    private fun isSystemInNightMode(): Boolean {
        val currentUiMode = resources.configuration.uiMode
        val nightMask = Configuration.UI_MODE_NIGHT_MASK
        return currentUiMode and nightMask == Configuration.UI_MODE_NIGHT_YES
    }
}