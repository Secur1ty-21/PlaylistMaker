package ru.yamost.playlistmaker.playlist.data

import android.net.Uri
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistEntity
import ru.yamost.playlistmaker.playlist.domain.model.Playlist

class PlaylistMapper {
    fun mapToDomain(playlistEntity: PlaylistEntity): Playlist {
        val uri = if (playlistEntity.photoUri == null) null else Uri.parse(playlistEntity.photoUri)
        return Playlist(
            imageUri = uri,
            name = playlistEntity.name,
            size = playlistEntity.size,
            id = playlistEntity.playlistId,
            description = playlistEntity.description
        )
    }
}