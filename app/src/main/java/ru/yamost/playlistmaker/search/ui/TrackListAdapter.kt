package ru.yamost.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yamost.playlistmaker.search.domain.model.Track

class TrackListAdapter(
    private val trackList: List<Track>
) : RecyclerView.Adapter<TrackListViewHolder>() {
    var itemClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder(parent)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { itemClickListener?.invoke(trackList[position]) }
    }
}