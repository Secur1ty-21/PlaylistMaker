package ru.yamost.playlistmaker.favorites.data

import ru.yamost.playlistmaker.favorites.data.db.entity.FavoriteTrackEntity
import ru.yamost.playlistmaker.search.domain.model.Track

class TrackMapper {
    fun mapToEntity(track: Track): FavoriteTrackEntity {
        return with(track) {
            FavoriteTrackEntity(
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

    fun mapToDomain(favoriteTrackEntity: FavoriteTrackEntity): Track {
        return with(favoriteTrackEntity) {
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
                previewUrl = previewUrl,
                isFavorite = true
            )
        }
    }
}