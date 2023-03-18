package ru.yamost.playlistmaker.presentation

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.data.model.Track
import ru.yamost.playlistmaker.presentation.search.SearchActivity
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private var track: Track? = null
    private lateinit var cover: ImageView
    private lateinit var collectionName: TextView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        intent.getParcelableExtra<Track>(SearchActivity.TRACK_ITEM_KEY)?.let {
            track = it
        }
        initViews()
        setDataToViews()
        topAppBar.setNavigationOnClickListener { finish() }
    }

    private fun initViews() {
        cover = findViewById(R.id.cover)
        trackName = findViewById(R.id.title)
        collectionName = findViewById(R.id.album)
        artistName = findViewById(R.id.artistName)
        duration = findViewById(R.id.duration)
        year = findViewById(R.id.year)
        genre = findViewById(R.id.genre)
        country = findViewById(R.id.country)
        topAppBar = findViewById(R.id.topAppBar)
    }

    private fun setDataToViews() {
        track?.let {
            Glide.with(cover)
                .load(it.artworkUrl?.replaceAfterLast('/', "512x512bb.jpg"))
                .centerCrop()
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.ic_track_placeholder)
                .into(cover)
            trackName.text = it.trackName
            collectionName.text = it.collectionName
            artistName.text = it.artistName
            duration.text = dateFormat.format(it.trackTimeMillis.toLong())
            year.text = it.releaseDate?.substringBefore('-')
            genre.text = it.primaryGenreName
            country.text = it.country
        }
    }
}