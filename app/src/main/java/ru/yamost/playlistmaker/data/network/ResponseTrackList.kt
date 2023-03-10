package ru.yamost.playlistmaker.data.network

import com.google.gson.annotations.SerializedName
import ru.yamost.playlistmaker.data.model.Track

class ResponseTrackList(@SerializedName("results") val trackList: List<Track>)