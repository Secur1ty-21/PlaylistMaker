package ru.yamost.playlistmaker.search.domain.api

import java.util.Locale

interface DateTimeRepository {
    fun getTrackTimeFormat(): String
    fun getPreferredLocale(): Locale
}