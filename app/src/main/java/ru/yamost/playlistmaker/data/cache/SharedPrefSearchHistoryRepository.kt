package ru.yamost.playlistmaker.data.cache

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.yamost.playlistmaker.domain.api.SearchHistoryRepository
import ru.yamost.playlistmaker.domain.model.Track

class SharedPrefSearchHistoryRepository(private val sharedPreferences: SharedPreferences) :
    SearchHistoryRepository {
    companion object {
        private const val SEARCH_HISTORY_KEY = "searchHistory"
        private const val MAX_TRACKS_IN_SEARCH_HISTORY = 10
    }

    private val gson = Gson()
    private var trackList = ArrayList<Track>()
    private val typeOfArrayList = object : TypeToken<ArrayList<Track>>() {}.type

    override fun saveTrack(track: Track) {
        val tracksJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        if (tracksJson != null) {
            trackList = gson.fromJson(tracksJson, typeOfArrayList)
            if (isAlreadyInHistory(track)) {
                trackList.remove(track)
            } else if (trackList.size == MAX_TRACKS_IN_SEARCH_HISTORY) {
                trackList.removeAt(trackList.size - 1)
            }
        }
        trackList.add(0, track)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, gson.toJson(trackList))
            .apply()
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
            return trackList
        }
        return listOf()
    }

    override fun isNotEmpty() = sharedPreferences.contains(SEARCH_HISTORY_KEY)
}