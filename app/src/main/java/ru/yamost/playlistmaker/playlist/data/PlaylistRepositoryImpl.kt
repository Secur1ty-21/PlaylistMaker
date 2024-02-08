package ru.yamost.playlistmaker.playlist.data

import android.content.ContentResolver
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.yamost.playlistmaker.playlist.data.db.dao.PlaylistDao
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistEntity
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistTrackEntity
import ru.yamost.playlistmaker.playlist.data.db.entity.TrackEntity
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistRepository
import ru.yamost.playlistmaker.playlist.domain.model.Playlist
import ru.yamost.playlistmaker.playlist.domain.model.PlaylistWithTracks
import ru.yamost.playlistmaker.search.domain.model.Track
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import kotlin.math.abs

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistMapper: PlaylistMapper,
    private val trackMapper: TrackMapper,
    private val internalDir: File,
    private val contentResolver: ContentResolver,
    private val trackSdf: SimpleDateFormat
) : PlaylistRepository {
    init {
        if (!internalDir.exists()) {
            internalDir.mkdirs()
        }
        trackSdf.timeZone = TimeZone.getTimeZone(GMT_ID)
    }

    override fun getPlaylistList(): Flow<List<Playlist>> = playlistDao.getPlaylistList().map {
        it.map { item -> playlistMapper.mapToDomain(item) }
    }

    override suspend fun addPlaylistToDatabase(name: String, description: String, uri: Uri?) {
        val internalUri = savePhotoToInternalDir(uri)
        playlistDao.createPlaylist(
            PlaylistEntity(
                name = name,
                description = description,
                photoUri = internalUri?.path,
            )
        )
    }

    private fun savePhotoToInternalDir(uriFile: Uri?): Uri? {
        return if (uriFile == null) {
            null
        } else {
            val filename = "${Date().time}.jpg"
            val newInternalFile = File(internalDir, filename)
            val inputStream = contentResolver.openInputStream(uriFile)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, newInternalFile.outputStream())

            newInternalFile.toUri()
        }
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val playlistTrackEntity = PlaylistTrackEntity(pId = playlist.id, tId = track.id)
        val trackEntity = trackMapper.mapToEntity(track)
        playlistDao.addTrackToPlaylist(playlistTrackEntity, trackEntity)
        playlistDao.updatePlaylistSize(newSize = playlist.size + 1, playlistId = playlist.id)
    }

    override fun isTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        return playlistDao.isTrackExistInPlaylist(playlistId = playlist.id, trackId = track.id)
    }

    override fun getTracksFromPlaylist(playlistId: Int) =
        playlistDao.getPlaylistWithTracks(playlistId).map { data ->
            if (data == null) {
                Result.failure(NotFoundException())
            } else {
                val playlist = playlistMapper.mapToDomain(data.playlist)
                val listCreatedDate = playlistDao.getPlaylistFromPlaylistTrackXrefTable(playlistId)
                val sortedTracks = sortTracksByAddedDate(
                    listCreatedDate, trackList = data.tracks
                )
                var minutes = 0L
                var seconds = 0L
                val tracks = sortedTracks.map {
                    val trackDuration = trackSdf.parse(it.trackTime)?.time ?: 0L
                    minutes += abs(trackDuration / ONE_MINUTE_IN_MILLIS)
                    seconds += abs(trackDuration % ONE_MINUTE_IN_MILLIS)
                    if (seconds > ONE_MINUTE_IN_MILLIS) {
                        ++minutes
                        seconds -= ONE_MINUTE_IN_MILLIS
                    }
                    trackMapper.mapToDomain(it)
                }
                Result.success(
                    PlaylistWithTracks(
                        playlist = playlist,
                        tracks = tracks,
                        playlistDuration = minutes.toString()
                    )
                )
            }
        }

    private fun sortTracksByAddedDate(listCreatedDate: List<PlaylistTrackEntity>, trackList: List<TrackEntity>): List<TrackEntity> {
        val sortedCreatedDateList = listCreatedDate.sortedByDescending { it.createdDate }
        val trackMap = mutableMapOf<Int, TrackEntity>()
        trackList.forEach { trackMap[it.trackId] = it }
        val sortedTracksByDate = mutableListOf<TrackEntity>()
        for (item in sortedCreatedDateList) {
            trackMap[item.tId]?.let {
                sortedTracksByDate.add(it)
            }
        }
        return sortedTracksByDate
    }

    override fun getPlaylist(playlistId: Int) = playlistDao.getPlaylist(playlistId).map {
        playlistMapper.mapToDomain(it)
    }

    override fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int, playlistSize: Int) {
        playlistDao.deleteTrackFromPlaylist(
            PlaylistTrackEntity(pId = playlistId, tId = trackId),
            playlistSize = playlistSize
        )
    }

    override fun deletePlaylist(playlistWithTracks: PlaylistWithTracks) {
        playlistWithTracks.playlist.imageUri?.let {
            it.path?.let {  path ->
                val photo = File(internalDir, path.substringAfterLast(File.separator))
                if (photo.exists()) {
                    photo.delete()
                }
            }
        }
        playlistDao.deletePlaylistWithTracks(
            playlistId = playlistWithTracks.playlist.id,
            tracksId = playlistWithTracks.tracks.map { it.id }
        )
    }

    override fun updatePlaylist(playlist: Playlist) {
        val internalUri = if (File(internalDir, playlist.imageUri?.path?.substringAfterLast(File.separator) ?: "").exists()) {
            playlist.imageUri
        } else {
            savePhotoToInternalDir(playlist.imageUri)
        }
        playlistDao.updatePlaylist(
            PlaylistEntity(
                playlistId = playlist.id,
                name = playlist.name,
                description = playlist.description,
                photoUri = internalUri?.path,
                size = playlist.size
            )
        )
    }

    companion object {
        private const val ONE_MINUTE_IN_MILLIS = 60000L
        private const val GMT_ID = "GMT"
    }
}