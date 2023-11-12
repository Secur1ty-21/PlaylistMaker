package ru.yamost.playlistmaker.presentation.screens.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.data.model.Track
import ru.yamost.playlistmaker.presentation.screens.search.SearchActivity
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val CURRENT_TIME_KEY = "CURRENT_TIME_KEY"
        private const val PLAYER_STATE_KEY = "PLAYER_STATE_KEY"
        private const val UPDATE_TIME_INTERVAL = 300L
    }

    private var savedCurrentTime = -1
    private var playerState = PlayerState.DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var handler: Handler
    private val updateTimeProgress = object : Runnable {
        override fun run() {
            if (playerState == PlayerState.PLAYING) {
                trackProgressTimeText.text = dateFormat.format(mediaPlayer.currentPosition)
                handler.postDelayed(this, UPDATE_TIME_INTERVAL)
            }
        }
    }
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private var track: Track? = null
    private lateinit var cover: ImageView
    private lateinit var collectionName: TextView
    private lateinit var captionAlbum: TextView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var playerButton: FloatingActionButton
    private lateinit var trackProgressTimeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        intent.getParcelableExtra<Track>(SearchActivity.TRACK_ITEM_KEY)?.let {
            track = it
        }
        preparePlayer()
        initViews()
        setDataToViews()
        topAppBar.setNavigationOnClickListener { finish() }
        playerButton.setOnClickListener { onClickPlayButton() }
        handler = Handler(Looper.getMainLooper())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedCurrentTime = mediaPlayer.currentPosition
        outState.putInt(CURRENT_TIME_KEY, savedCurrentTime)
        outState.putInt(PLAYER_STATE_KEY, playerState.ordinal)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedCurrentTime = savedInstanceState.getInt(CURRENT_TIME_KEY)
        playerState = PlayerState.values()[savedInstanceState.getInt(PLAYER_STATE_KEY)]
        trackProgressTimeText.text = dateFormat.format(savedCurrentTime)
    }

    private fun preparePlayer() {
        with(mediaPlayer) {
            track?.previewUrl?.let { url ->
                setDataSource(url)
            }
            setOnPreparedListener {
                playerButton.isEnabled = true
                if (savedCurrentTime != -1) {
                    seekTo(savedCurrentTime)
                }
                playerState = PlayerState.PREPARED
            }
            setOnCompletionListener {
                trackProgressTimeText.text = dateFormat.format(0)
                savedCurrentTime = -1
                playerState = PlayerState.PREPARED
                playerButton.setImageResource(R.drawable.ic_play_circle)
            }
            prepareAsync()
        }
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
        captionAlbum = findViewById(R.id.captionAlbum)
        playerButton = findViewById(R.id.playButton)
        trackProgressTimeText = findViewById(R.id.trackProgressTime)
    }

    private fun setDataToViews() {
        track?.let {
            Glide.with(cover)
                .load(it.artworkUrl?.replaceAfterLast('/', "512x512bb.jpg"))
                .centerCrop()
                .transform(RoundedCorners(cover.resources.getDimensionPixelSize(R.dimen.cornerRadiusS)))
                .placeholder(R.drawable.ic_track_placeholder)
                .into(cover)
            trackName.text = it.trackName
            if (it.collectionName.isNullOrEmpty()) {
                collectionName.isVisible = false
                captionAlbum.isVisible = false
            } else {
                collectionName.text = it.collectionName
            }
            artistName.text = it.artistName
            duration.text = dateFormat.format(it.trackTimeMillis.toLong())
            year.text = it.releaseDate?.substringBefore('-')
            genre.text = it.primaryGenreName
            country.text = it.country
        }
    }

    private fun onClickPlayButton() {
        when (playerState) {
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }
            PlayerState.PLAYING -> {
                pausePlayer()
            }
            PlayerState.DEFAULT -> {}
        }
    }

    private fun startPlayer() {
        playerState = PlayerState.PLAYING
        mediaPlayer.start()
        playerButton.setImageResource(R.drawable.ic_pause_circle)
        handler.postDelayed(updateTimeProgress, UPDATE_TIME_INTERVAL)
    }

    private fun pausePlayer() {
        playerState = PlayerState.PAUSED
        handler.removeCallbacks(updateTimeProgress)
        mediaPlayer.pause()
        playerButton.setImageResource(R.drawable.ic_play_circle)
    }

    override fun onPause() {
        super.onPause()
        if (playerState == PlayerState.PLAYING) {
            pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeProgress)
        mediaPlayer.release()
    }
}