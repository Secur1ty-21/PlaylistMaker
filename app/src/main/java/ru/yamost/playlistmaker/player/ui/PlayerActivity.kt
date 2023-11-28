package ru.yamost.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.databinding.ActivityPlayerBinding
import ru.yamost.playlistmaker.player.ui.model.PlayerScreenState
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.search.ui.SearchActivity

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track: Track? = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(SearchActivity.TRACK_ITEM_KEY, Track::class.java)
        } else {
            intent.getParcelableExtra(SearchActivity.TRACK_ITEM_KEY)
        }
        viewModel = ViewModelProvider(
            this, PlayerViewModel.getViewModelFactory(track)
        )[PlayerViewModel::class.java]
        viewModel.playerScreenState.observe(this) {
            renderPlayerState(it)
        }
        viewModel.trackInfoState.observe(this) {
            renderTrackInfo(it)
        }
        binding.topAppBar.setNavigationOnClickListener { finish() }
        binding.playButton.setOnClickListener {
            viewModel.onPlayButton()
        }
    }

    private fun renderPlayerState(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.PlayButtonState -> {
                binding.playButton.setImageResource(state.drawableRes)
                binding.playButton.isEnabled = state.isEnabled
                state.playedTime?.let { binding.trackProgressTime.text = it }
            }

            is PlayerScreenState.PlayedTime -> {
                binding.trackProgressTime.text = state.playedTime
            }
        }
    }

    private fun renderTrackInfo(track: Track?) {
        track?.let {
            Glide.with(binding.cover).load(it.artworkUrl).centerCrop()
                .transform(RoundedCorners(binding.cover.resources.getDimensionPixelSize(R.dimen.cornerRadiusS)))
                .placeholder(R.drawable.ic_track_placeholder).into(binding.cover)
            binding.title.text = it.name
            if (it.collection.isEmpty()) {
                binding.album.isVisible = false
                binding.captionAlbum.isVisible = false
            } else {
                binding.album.text = it.collection
            }
            binding.artistName.text = it.artist
            binding.duration.text = it.time
            binding.year.text = it.releaseDate
            binding.genre.text = it.primaryGenreName
            binding.country.text = it.country
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}