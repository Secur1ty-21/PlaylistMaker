package ru.yamost.playlistmaker.playlist.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithTracksDto(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        entity = TrackEntity::class,
        parentColumn = "playlist_id",
        entityColumn = "track_id",
        associateBy = Junction(
            value = PlaylistTrackEntity::class,
            parentColumn = "p_id",
            entityColumn = "t_id"
        )
    )
    val tracks: List<TrackEntity>
)