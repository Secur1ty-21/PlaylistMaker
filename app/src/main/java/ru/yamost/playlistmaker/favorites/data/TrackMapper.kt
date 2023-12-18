package ru.yamost.playlistmaker.favorites.data

import ru.yamost.playlistmaker.favorites.data.db.entity.TrackEntity
import ru.yamost.playlistmaker.search.domain.model.Track

class TrackMapper {
    fun mapToEntity(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
                id = id,
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

    fun mapToDomain(trackEntity: TrackEntity): Track {
        return with(trackEntity) {
            Track(
                id = id,
                name = trackName,
                artist = artistName,
                time = trackTime,
                collection = collectionName,
                artworkUrl = artworkUrl,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl
            )
        }
    }
}