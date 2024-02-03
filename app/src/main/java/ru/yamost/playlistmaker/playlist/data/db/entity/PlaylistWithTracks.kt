package ru.yamost.playlistmaker.playlist.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.yamost.playlistmaker.favorites.data.db.entity.TrackEntity

data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        entity = TrackEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PlaylistTrackEntity::class,
            parentColumn = "playlist_id",
            entityColumn = "track_id"
        )
    )
    val tracks: List<TrackEntity>
)
