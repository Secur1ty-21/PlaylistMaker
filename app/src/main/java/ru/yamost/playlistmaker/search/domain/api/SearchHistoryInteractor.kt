package ru.yamost.playlistmaker.search.domain.api

import ru.yamost.playlistmaker.search.domain.model.Track

interface SearchHistoryInteractor {
    fun saveTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
    fun isHistoryExist(): Boolean
}