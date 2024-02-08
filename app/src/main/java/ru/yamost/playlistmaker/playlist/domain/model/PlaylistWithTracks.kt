package ru.yamost.playlistmaker.playlist.domain.model

import ru.yamost.playlistmaker.search.domain.model.Track

class PlaylistWithTracks(
    val playlist: Playlist,
    val tracks: List<Track>,
    val playlistDuration: String
)