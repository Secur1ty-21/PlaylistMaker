package ru.yamost.playlistmaker.playlist.domain.impl

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistRepository
import ru.yamost.playlistmaker.playlist.domain.model.Playlist
import ru.yamost.playlistmaker.playlist.domain.model.PlaylistWithTracks
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

    override fun getPlaylistWithTracks(playlistId: Int) = playlistRepository.getTracksFromPlaylist(playlistId)

    override fun getPlaylist(playlistId: Int) = playlistRepository.getPlaylist(playlistId)

    override fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int, playlistSize: Int) {
        playlistRepository.deleteTrackFromPlaylist(playlistId, trackId, playlistSize)
    }

    override fun deletePlaylist(playlistWithTracks: PlaylistWithTracks) {
        playlistRepository.deletePlaylist(playlistWithTracks)
    }

    override fun updatePlaylist(playlistId: Int, name: String, description: String, uri: Uri?, size: Int) {
        playlistRepository.updatePlaylist(
            Playlist(
                id = playlistId,
                imageUri = uri,
                name = name,
                description = description,
                size = size
            )
        )
    }
}