package ru.yamost.playlistmaker.domain.api

import java.util.Locale

interface DateTimeRepository {
    fun getTrackTimeFormat(): String
    fun getPreferredLocale(): Locale
}