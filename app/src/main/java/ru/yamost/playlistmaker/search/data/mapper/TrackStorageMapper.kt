package ru.yamost.playlistmaker.search.data.mapper

import ru.yamost.playlistmaker.search.data.dto.TrackStorageDto
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Mapper

class TrackStorageMapper: Mapper<List<Track>, List<TrackStorageDto>> {
    override fun toModel(value: List<Track>): List<TrackStorageDto> {
        return value.map {
            TrackStorageDto(
                id = it.id,
                trackName = it.name,
                artistName = it.artist,
                trackTime = it.time,
                artworkUrl = it.artworkUrl,
                collectionName = it.collection,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl
            )
        }
    }

    override fun fromModel(value: List<TrackStorageDto>): List<Track> {
        return value.map {
            Track(
                id = it.id,
                name = it.trackName,
                artist = it.artistName,
                time = it.trackTime,
                artworkUrl = it.artworkUrl,
                collection = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl
            )
        }
    }
}