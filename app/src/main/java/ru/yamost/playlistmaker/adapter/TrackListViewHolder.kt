package ru.yamost.playlistmaker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.data.model.Track
import java.text.SimpleDateFormat

class TrackListViewHolder(parentView: ViewGroup, private val dateFormat: SimpleDateFormat) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parentView.context)
            .inflate(R.layout.item_track, parentView, false)
    ) {

    private val coverTrack: ImageView = itemView.findViewById(R.id.cover)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(track: Track) {
        itemView.setOnClickListener { }
        Glide.with(itemView)
            .load(track.artworkUrl)
            .placeholder(R.drawable.ic_track_placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(coverTrack)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = trackTime.context.getString(
            R.string.time_of_track,
            dateFormat.format(track.trackTimeMillis.toLong())
        )
    }
}