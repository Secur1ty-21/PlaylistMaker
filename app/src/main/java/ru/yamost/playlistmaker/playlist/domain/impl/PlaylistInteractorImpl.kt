package ru.yamost.playlistmaker.playlist.domain.impl

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistRepository
import ru.yamost.playlistmaker.playlist.domain.model.Playlist
import ru.yamost.playlistmaker.search.domain.model.Track

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override fun getPlaylistList(): Flow<List<Playlist>> = playlistRepository.getPlaylistList()
    override suspend fun createPlaylist(name: String, description: String, uri: Uri?) {
        playlistRepository.addPlaylistToDatabase(uri = uri, description = description, name = name)
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override fun isTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        return playlistRepository.isTrackInPlaylist(track, playlist)
    }
}