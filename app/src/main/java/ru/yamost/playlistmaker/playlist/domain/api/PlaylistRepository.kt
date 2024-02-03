package ru.yamost.playlistmaker.playlist.domain.api

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.playlist.domain.model.Playlist

interface PlaylistRepository {
    fun getPlaylistList(): Flow<List<Playlist>>
    suspend fun addPlaylistToDatabase(name: String, description: String, uri: Uri?)
}