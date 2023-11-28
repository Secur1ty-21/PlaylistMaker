package ru.yamost.playlistmaker.search.domain.api

import ru.yamost.playlistmaker.search.domain.model.Track

interface SearchHistoryRepository {
    fun getHistory(): List<Track>
    fun clear()
    fun saveTrack(track: Track)
    fun isNotEmpty(): Boolean
}