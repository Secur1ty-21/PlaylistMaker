package ru.yamost.playlistmaker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.model.Track

class TrackListAdapter(
    private val trackList: List<Track>
) : RecyclerView.Adapter<TrackListAdapter.TrackListViewHolder>() {

    class TrackListViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parentView.context)
            .inflate(R.layout.item_track, parentView, false)
    ) {

        private val coverTrack: ImageView = itemView.findViewById(R.id.cover_track)
        private val trackName: TextView = itemView.findViewById(R.id.title_track)
        private val subtitleTrack: TextView = itemView.findViewById(R.id.subtitle_track)

        fun bind(track: Track) {
            itemView.setOnClickListener { }
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.ic_track_placeholder)
                .centerCrop()
                .transform(RoundedCorners(2))
                .into(coverTrack)
            trackName.text = track.trackName
            subtitleTrack.text = itemView.context.getString(
                R.string.subtitle_of_track,
                track.artistName,
                track.trackTime
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder(parent)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
}