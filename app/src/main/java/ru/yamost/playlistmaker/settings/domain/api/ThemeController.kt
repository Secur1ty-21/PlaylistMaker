package ru.yamost.playlistmaker.settings.domain.api

interface ThemeController {
    fun switchTheme(isDarkTheme: Boolean)
    fun isSystemInDarkMode(uiMode: Int): Boolean
}