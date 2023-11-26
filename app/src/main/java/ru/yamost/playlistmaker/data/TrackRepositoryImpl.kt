package ru.yamost.playlistmaker.data

import ru.yamost.playlistmaker.data.dto.TrackSearchRequest
import ru.yamost.playlistmaker.data.dto.TrackSearchResponse
import ru.yamost.playlistmaker.domain.api.DateTimeRepository
import ru.yamost.playlistmaker.domain.api.TrackRepository
import ru.yamost.playlistmaker.domain.model.SearchStatus
import ru.yamost.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat

class TrackRepositoryImpl(
    dateTimeRepository: DateTimeRepository,
    private val networkClient: NetworkClient
) : TrackRepository {
    private val formatter = SimpleDateFormat(
        dateTimeRepository.getTrackTimeFormat(),
        dateTimeRepository.getPreferredLocale()
    )

    override fun searchTracks(searchQuery: String): Pair<List<Track>, SearchStatus> {
        val response = networkClient.doSearchTrackRequest(TrackSearchRequest(searchQuery))
        if (response.resultCode == 200) {
            val trackList = (response as TrackSearchResponse).trackList.map { dto ->
                Track(
                    id = dto.id,
                    name = dto.trackName,
                    artist = dto.artistName,
                    time = formatter.format(dto.trackTimeMillis.toLong()),
                    artworkUrl = dto.artworkUrl ?: "",
                    collection = dto.collectionName ?: "",
                    releaseDate = dto.releaseDate ?: "",
                    primaryGenreName = dto.primaryGenreName ?: "",
                    country = dto.country ?: "",
                    previewUrl = dto.previewUrl ?: ""
                )
            }
            val status = if (trackList.isEmpty()) {
                SearchStatus.EMPTY_RESULT
            } else {
                SearchStatus.SUCCESS
            }
            return Pair(trackList, status)
        } else {
            return Pair(emptyList(), SearchStatus.CONNECTION_ERROR)
        }
    }
}