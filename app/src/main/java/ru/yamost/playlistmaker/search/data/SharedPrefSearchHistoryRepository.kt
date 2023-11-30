package ru.yamost.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.yamost.playlistmaker.search.data.dto.TrackStorageDto
import ru.yamost.playlistmaker.search.domain.api.SearchHistoryRepository
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Mapper

class SharedPrefSearchHistoryRepository(
    private val mapper: Mapper<List<Track>, List<TrackStorageDto>>,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SearchHistoryRepository {
    companion object {
        private const val SEARCH_HISTORY_KEY = "searchHistory"
        private const val MAX_TRACKS_IN_SEARCH_HISTORY = 10
    }

    private var trackList = ArrayList<TrackStorageDto>()
    private val typeOfArrayList = object : TypeToken<ArrayList<TrackStorageDto>>() {}.type

    override fun saveTrack(track: Track) {
        val mappedTrack = mapper.toModel(listOf(track)).first()
        val tracksJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        if (tracksJson != null) {
            trackList = gson.fromJson(tracksJson, typeOfArrayList)
            if (isAlreadyInHistory(track)) {
                trackList.remove(mappedTrack)
            } else if (trackList.size == MAX_TRACKS_IN_SEARCH_HISTORY) {
                trackList.removeAt(trackList.size - 1)
            }
        }
        trackList.add(0, mappedTrack)
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, gson.toJson(trackList)).apply()
    }

    private fun isAlreadyInHistory(track: Track): Boolean {
        val length = trackList.size
        for (i in 0 until length) {
            if (track.id == trackList[i].id) {
                return true
            }
        }
        return false
    }

    override fun clear() {
        trackList.clear()
        sharedPreferences.edit().clear().apply()
    }

    override fun getHistory(): List<Track> {
        val tracksJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        if (tracksJson != null) {
            trackList = gson.fromJson(tracksJson, typeOfArrayList)
            return mapper.fromModel(trackList)
        }
        return listOf()
    }

    override fun isNotEmpty() = sharedPreferences.contains(SEARCH_HISTORY_KEY)
}