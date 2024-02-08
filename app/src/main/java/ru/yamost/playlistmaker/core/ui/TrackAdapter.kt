package ru.yamost.playlistmaker.core.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.yamost.playlistmaker.search.domain.model.Track

class TrackAdapter(
    private val onItemClick: (Track) -> Unit,
    private val onItemLongClick: (Track) -> Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val trackList = mutableListOf<Track>()

    fun updateData(newList: List<Track>) {
        val diffCallback = TrackDiffCallback(oldList = trackList, newList = newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        trackList.clear()
        trackList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrackViewHolder(onItemClick, onItemLongClick, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TrackViewHolder) {
            holder.bind(track = trackList[position])
        }
    }

    override fun getItemCount() = trackList.size
}