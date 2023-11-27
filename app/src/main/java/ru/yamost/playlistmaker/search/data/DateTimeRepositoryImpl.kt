package ru.yamost.playlistmaker.search.data

import ru.yamost.playlistmaker.search.domain.api.DateTimeRepository
import java.util.Locale

class DateTimeRepositoryImpl : DateTimeRepository {
    override fun getTrackTimeFormat() = "mm:ss"

    override fun getPreferredLocale(): Locale = Locale.getDefault()
}