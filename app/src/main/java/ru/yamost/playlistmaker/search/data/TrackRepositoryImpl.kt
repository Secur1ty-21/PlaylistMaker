package ru.yamost.playlistmaker.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.yamost.playlistmaker.favorites.domain.api.FavoriteTrackRepository
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchRequest
import ru.yamost.playlistmaker.search.data.network.dto.TrackSearchResponse
import ru.yamost.playlistmaker.search.domain.api.TrackRepository
import ru.yamost.playlistmaker.search.domain.model.SearchErrorStatus
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Resource
import java.text.SimpleDateFormat

class TrackRepositoryImpl(
    private val favoriteTrackRepository: FavoriteTrackRepository,
    private val formatter: SimpleDateFormat,
    private val networkClient: NetworkClient
) : TrackRepository {

    override fun searchTracks(searchQuery: String): Flow<Resource<List<Track>, SearchErrorStatus>> =
        flow {
            val response = networkClient.doSearchTrackRequest(TrackSearchRequest(searchQuery))
            when (response.resultCode) {
                200 -> {
                    val favoriteTrackMap = favoriteTrackRepository.getFavoriteIdMap()
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
                            previewUrl = dto.previewUrl ?: "",
                            isFavorite = favoriteTrackMap.contains(dto.id)
                        )
                    }
                    if (trackList.isEmpty()) {
                        emit(Resource.Error(errorStatus = SearchErrorStatus.EMPTY_RESULT))
                    } else {
                        emit(Resource.Success(data = trackList))
                    }
                }

                NetworkClient.CANCEL_REQUEST_CODE -> {
                    emit(Resource.Error(SearchErrorStatus.CANCELED))
                }

                else -> {
                    emit(Resource.Error(SearchErrorStatus.CONNECTION_ERROR))
                }
            }
        }
}