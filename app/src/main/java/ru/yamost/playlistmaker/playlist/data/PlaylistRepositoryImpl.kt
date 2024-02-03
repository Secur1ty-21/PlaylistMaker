package ru.yamost.playlistmaker.playlist.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.yamost.playlistmaker.playlist.data.db.dao.PlaylistDao
import ru.yamost.playlistmaker.playlist.data.db.entity.PlaylistEntity
import ru.yamost.playlistmaker.playlist.domain.api.PlaylistRepository
import ru.yamost.playlistmaker.playlist.domain.model.Playlist
import java.io.File
import java.util.Date

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistMapper: PlaylistMapper,
    private val internalDir: File,
    private val contentResolver: ContentResolver
) : PlaylistRepository {
    init {
        if (!internalDir.exists()) {
            internalDir.mkdirs()
        }
    }

    override fun getPlaylistList(): Flow<List<Playlist>> = playlistDao.getPlaylistList().map {
        it.map { item -> playlistMapper.mapToDomain(item) }
    }

    override suspend fun addPlaylistToDatabase(name: String, description: String, uri: Uri?) {
        Log.v(Playlist::class.java.simpleName, "uri = $uri")
        Log.v(Playlist::class.java.simpleName, "uri_path = ${uri?.path}")
        Log.v(Playlist::class.java.simpleName, "uri_some = ${uri?.encodedPath}")
        val internalUri = saveFile(uri)
        playlistDao.createPlaylist(
            PlaylistEntity(
                name = name,
                description = description,
                photoUri = internalUri?.path,
            )
        )
    }

    private fun saveFile(uriFile: Uri?): Uri? {
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
}