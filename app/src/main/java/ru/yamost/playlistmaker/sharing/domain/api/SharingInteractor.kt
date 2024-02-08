package ru.yamost.playlistmaker.sharing.domain.api

import ru.yamost.playlistmaker.playlist.domain.model.PlaylistWithTracks

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlaylist(playlistWithTracks: PlaylistWithTracks)
}