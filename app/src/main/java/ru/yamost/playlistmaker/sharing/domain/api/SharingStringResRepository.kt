package ru.yamost.playlistmaker.sharing.domain.api

import ru.yamost.playlistmaker.playlist.domain.model.PlaylistWithTracks
import ru.yamost.playlistmaker.sharing.domain.model.EmailData

interface SharingStringResRepository {
    val appLink: String
    val termsLink: String
    val supportEmailData: EmailData
    fun getPlaylistStringForShare(playlistWithTracks: PlaylistWithTracks): String
}