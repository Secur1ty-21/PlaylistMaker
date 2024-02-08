package ru.yamost.playlistmaker.playlist.domain.api

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.playlist.domain.model.Playlist
import ru.yamost.playlistmaker.playlist.domain.model.PlaylistWithTracks
import ru.yamost.playlistmaker.search.domain.model.Track

interface PlaylistRepository {
    fun getPlaylistList(): Flow<List<Playlist>>
    suspend fun addPlaylistToDatabase(name: String, description: String, uri: Uri?)
    fun addTrackToPlaylist(track: Track, playlist: Playlist)
    fun isTrackInPlaylist(track: Track, playlist: Playlist): Boolean
    fun getTracksFromPlaylist(playlistId: Int): Flow<Result<PlaylistWithTracks>>
    fun getPlaylist(playlistId: Int): Flow<Playlist>
    fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int, playlistSize: Int)
    fun deletePlaylist(playlistWithTracks: PlaylistWithTracks)
    fun updatePlaylist(playlist: Playlist)
}