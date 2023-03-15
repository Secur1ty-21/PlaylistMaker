package ru.yamost.playlistmaker.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yamost.playlistmaker.data.model.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackListAdapter(
    private val trackList: List<Track>
) : RecyclerView.Adapter<TrackListViewHolder>() {
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    var itemClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder(parent, dateFormat)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { itemClickListener?.invoke(trackList[position]) }
    }
}