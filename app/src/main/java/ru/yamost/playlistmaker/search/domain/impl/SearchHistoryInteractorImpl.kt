package ru.yamost.playlistmaker.search.domain.impl

import ru.yamost.playlistmaker.search.domain.api.SearchHistoryInteractor
import ru.yamost.playlistmaker.search.domain.api.SearchHistoryRepository
import ru.yamost.playlistmaker.search.domain.model.Track

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {
    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }

    override fun clearHistory() {
        repository.clear()
    }

    override fun isHistoryExist() = repository.isNotEmpty()
}