package ru.yamost.playlistmaker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PlayerInteractorState(
    val playedTime: Int = 0,
    val playerState: PlayerState = PlayerState.DEFAULT
) : Parcelable