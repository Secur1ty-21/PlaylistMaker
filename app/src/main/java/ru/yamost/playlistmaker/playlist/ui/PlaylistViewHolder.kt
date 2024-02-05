package ru.yamost.playlistmaker.playlist.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.playlist.domain.model.Playlist

class PlaylistViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val cover = itemView.findViewById<ImageView>(R.id.img_cover)
    private val lblAlbumName = itemView.findViewById<TextView>(R.id.lbl_name)
    private val lblAlbumSize = itemView.findViewById<TextView>(R.id.lbl_size)

    fun bind(playlist: Playlist) {
        lblAlbumName.text = playlist.name
        lblAlbumSize.text = itemView.context.resources.getQuantityString(
            R.plurals.playlist_item_size,
            playlist.size,
            playlist.size
        )
        if (playlist.imageUri == null) {
            cover.setImageDrawable(
                AppCompatResources.getDrawable(
                    itemView.context,
                    R.drawable.ic_track_placeholder
                )
            )
        } else {
            cover.setImageURI(playlist.imageUri)
        }
    }
}