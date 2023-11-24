package ru.yamost.playlistmaker.domain.api

import ru.yamost.playlistmaker.domain.model.Track

interface SearchHistoryRepository {
    fun getHistory(): List<Track>
    fun clear()
    fun saveTrack(track: Track)
    fun isNotEmpty(): Boolean
}