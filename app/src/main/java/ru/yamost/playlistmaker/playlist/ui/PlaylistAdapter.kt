package ru.yamost.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.playlist.domain.model.Playlist

class PlaylistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val playlistList = mutableListOf<Playlist>()

    fun updateContent(data: List<Playlist>) {
        playlistList.clear()
        playlistList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grid_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PlaylistViewHolder) {
            holder.bind(playlist = playlistList[position])
        }
    }

    override fun getItemCount() = playlistList.size
}