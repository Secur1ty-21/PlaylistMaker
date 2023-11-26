package ru.yamost.playlistmaker.data

import ru.yamost.playlistmaker.domain.api.DateTimeRepository
import java.util.Locale

class DateTimeRepositoryImpl : DateTimeRepository {
    override fun getTrackTimeFormat() = "mm:ss"

    override fun getPreferredLocale(): Locale = Locale.getDefault()
}