package ru.yamost.playlistmaker.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.player.presentation.ui.PlayerPlaylistViewHolder
import ru.yamost.playlistmaker.playlist.domain.model.Playlist
import ru.yamost.playlistmaker.playlist.ui.PlaylistViewHolder

class PlaylistAdapter(
    private val isLinearType: Boolean = false, private val onItemClick: (Playlist) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val playlistList = mutableListOf<Playlist>()

    fun updateContent(data: List<Playlist>) {
        val diff = PlaylistDiffCallback(playlistList, data)
        val result = DiffUtil.calculateDiff(diff)
        playlistList.clear()
        playlistList.addAll(data)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId =
            if (isLinearType) R.layout.item_linear_playlist else R.layout.item_grid_playlist
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return if (isLinearType) {
            PlayerPlaylistViewHolder(view, onItemClick)
        } else {
            PlaylistViewHolder(view, onItemClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlaylistViewHolder -> {
                holder.bind(playlist = playlistList[position])
            }

            is PlayerPlaylistViewHolder -> {
                holder.bind(playlist = playlistList[position])
            }
        }
    }

    override fun getItemCount() = playlistList.size
}