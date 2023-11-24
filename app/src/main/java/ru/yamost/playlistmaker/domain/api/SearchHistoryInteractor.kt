package ru.yamost.playlistmaker.domain.api

import ru.yamost.playlistmaker.domain.model.Track

interface SearchHistoryInteractor {
    fun saveTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
    fun isHistoryExist(): Boolean
}