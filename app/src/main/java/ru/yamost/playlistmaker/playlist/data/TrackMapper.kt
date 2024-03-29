package ru.yamost.playlistmaker.playlist.data

import ru.yamost.playlistmaker.playlist.data.db.entity.TrackEntity
import ru.yamost.playlistmaker.search.domain.model.Track

class TrackMapper {
    fun mapToEntity(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
                idTrack = id,
                trackName = name,
                artistName = artist,
                trackTime = time,
                collectionName = collection,
                artworkUrl = artworkUrl,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl
            )
        }
    }
}