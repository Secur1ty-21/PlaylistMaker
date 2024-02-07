package ru.yamost.playlistmaker.core.ui

import androidx.recyclerview.widget.DiffUtil
import ru.yamost.playlistmaker.playlist.domain.model.Playlist

class PlaylistDiffCallback(
    private val oldList: List<Playlist>, private val newList: List<Playlist>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}