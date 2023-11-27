package ru.yamost.playlistmaker.search.data

import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchRequest
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchResponse
import ru.yamost.playlistmaker.search.domain.api.DateTimeRepository
import ru.yamost.playlistmaker.search.domain.api.TrackRepository
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Resource
import java.text.SimpleDateFormat

class TrackRepositoryImpl(
    dateTimeRepository: DateTimeRepository,
    private val networkClient: NetworkClient
) : TrackRepository {
    private val formatter = SimpleDateFormat(
        dateTimeRepository.getTrackTimeFormat(),
        dateTimeRepository.getPreferredLocale()
    )

    override fun searchTracks(searchQuery: String): Resource<List<Track>, SearchErrorStatus> {
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
            if (trackList.isEmpty()) {
                return Resource.Error(errorStatus = SearchErrorStatus.EMPTY_RESULT)
            }
            return Resource.Success(data = trackList)
        } else {
            return Resource.Error(errorStatus = SearchErrorStatus.CONNECTION_ERROR)
        }
    }
}