package ru.yamost.playlistmaker.ui.player

import android.os.Build
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
import ru.yamost.playlistmaker.Creator
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.domain.api.PlayerInteractor
import ru.yamost.playlistmaker.domain.model.PlayerInteractorState
import ru.yamost.playlistmaker.domain.model.PlayerState
import ru.yamost.playlistmaker.domain.model.Track
import ru.yamost.playlistmaker.ui.search.SearchActivity

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val CURRENT_TIME_KEY = "CURRENT_TIME_KEY"
        private const val PLAYER_STATE_KEY = "PLAYER_STATE_KEY"
        private const val UPDATE_TIME_INTERVAL = 200L
    }

    private lateinit var handler: Handler
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
    private var interactor: PlayerInteractor? = null
    private var track: Track? = null
    private var savedPlayerState: PlayerState? = null
    private var savedCurrentTime = -1
    private val updateTimeProgress = object : Runnable {
        override fun run() {
            if (interactor?.currentState == PlayerState.PLAYING) {
                trackProgressTimeText.text = interactor?.formatPlayedTime() ?:
                    getString(R.string.lbl_played_time_zero)
                handler.postDelayed(this, UPDATE_TIME_INTERVAL)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        if (intent.hasExtra(SearchActivity.TRACK_ITEM_KEY)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(SearchActivity.TRACK_ITEM_KEY, Track::class.java)?.let {
                    track = it
                }
            } else {
                intent.getParcelableExtra<Track>(SearchActivity.TRACK_ITEM_KEY)?.let {
                    track = it
                }
            }
        }
        initInteractor()
        initViews()
        setDataToViews()
        topAppBar.setNavigationOnClickListener { finish() }
        playerButton.setOnClickListener { onClickPlayButton() }
        handler = Handler(Looper.getMainLooper())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedCurrentTime = interactor?.playedTime ?: -1
        outState.putInt(CURRENT_TIME_KEY, savedCurrentTime)
        outState.putInt(
            PLAYER_STATE_KEY,
            interactor?.currentState?.ordinal ?: PlayerState.DEFAULT.ordinal
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedCurrentTime = savedInstanceState.getInt(CURRENT_TIME_KEY)
        savedPlayerState = PlayerState.values()[savedInstanceState.getInt(PLAYER_STATE_KEY)]
        trackProgressTimeText.text = interactor?.formatPlayedTime() ?:
            getString(R.string.lbl_played_time_zero)
    }

    private fun initInteractor() {
        track?.previewUrl?.let { url ->
            interactor = Creator.providePlayerInteractor(url)
            interactor?.prepare(consumer = object : PlayerInteractor.PlayerConsumer {
                override fun onReadyForUse() {
                    playerButton.isEnabled = true
                    interactor?.restore(
                        PlayerInteractorState(
                            playedTime = savedCurrentTime,
                            playerState = savedPlayerState ?: PlayerState.PREPARED
                        )
                    )
                    trackProgressTimeText.text = interactor?.formatPlayedTime() ?:
                        getString(R.string.lbl_played_time_zero)
                }

                override fun onTrackEnd() {
                    trackProgressTimeText.text = getString(R.string.lbl_played_time_final)
                    playerButton.setImageResource(R.drawable.ic_play_circle)
                }
            })
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
                .load(it.artworkUrl)
                .centerCrop()
                .transform(RoundedCorners(cover.resources.getDimensionPixelSize(R.dimen.cornerRadiusS)))
                .placeholder(R.drawable.ic_track_placeholder)
                .into(cover)
            trackName.text = it.name
            if (it.collection.isEmpty()) {
                collectionName.isVisible = false
                captionAlbum.isVisible = false
            } else {
                collectionName.text = it.collection
            }
            artistName.text = it.artist
            duration.text = it.time
            year.text = it.releaseDate.substringBefore('-')
            genre.text = it.primaryGenreName
            country.text = it.country
        }
    }

    private fun onClickPlayButton() {
        interactor?.let {
            when (it.currentState) {
                PlayerState.PREPARED, PlayerState.PAUSED -> {
                    startPlayer()
                }

                PlayerState.PLAYING -> {
                    pausePlayer()
                }

                PlayerState.DEFAULT -> {  }
            }
        }
    }

    private fun startPlayer() {
        interactor?.play()
        playerButton.setImageResource(R.drawable.ic_pause_circle)
        handler.postDelayed(updateTimeProgress, UPDATE_TIME_INTERVAL)
    }

    private fun pausePlayer() {
        interactor?.pause()
        handler.removeCallbacks(updateTimeProgress)
        playerButton.setImageResource(R.drawable.ic_play_circle)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeProgress)
        interactor?.clearMemory()
    }
}