package ru.yamost.playlistmaker.sharing.data

import android.content.Context
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.playlist.domain.model.PlaylistWithTracks
import ru.yamost.playlistmaker.sharing.domain.api.SharingStringResRepository
import ru.yamost.playlistmaker.sharing.domain.model.EmailData

class SharingStringResRepositoryImpl(
    private val context: Context
) : SharingStringResRepository {
    override val appLink = context.getString(R.string.url_share)
    override val termsLink = context.getString(R.string.url_agreement)
    override val supportEmailData = EmailData(
        email = context.getString(R.string.support_email),
        title = context.getString(R.string.support_email_subject),
        message = context.getString(R.string.support_email_text)
    )

    override fun getPlaylistStringForShare(playlistWithTracks: PlaylistWithTracks): String {
        val playlist = playlistWithTracks.playlist
        val tracks = playlistWithTracks.tracks
        var playlistString = "${playlist.name}\n${playlist.description}\n"
        playlistString += context.resources.getQuantityString(
            R.plurals.playlist_item_size, playlist.size, playlist.size
        )
        for (i in tracks.indices) {
            playlistString += "\n"
            playlistString += context.getString(
                R.string.share_playlist_template,
                i + 1,
                tracks[i].artist,
                tracks[i].name,
                tracks[i].time
            )
        }
        return playlistString
    }
}