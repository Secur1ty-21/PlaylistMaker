package ru.yamost.playlistmaker.playlist.domain.api

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.playlist.domain.model.Playlist

interface PlaylistInteractor {
    fun getPlaylistList(): Flow<List<Playlist>>
    suspend fun createPlaylist(name: String, description: String, uri: Uri?)
}