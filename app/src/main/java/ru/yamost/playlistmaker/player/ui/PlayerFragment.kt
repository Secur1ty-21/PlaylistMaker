package ru.yamost.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.databinding.FragmentPlayerBinding
import ru.yamost.playlistmaker.player.ui.model.PlayerScreenState
import ru.yamost.playlistmaker.search.domain.model.Track

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = _binding!!
    private var track: Track? = null
    private val viewModel by viewModel<PlayerViewModel> { parametersOf(track) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            track = PlayerFragmentArgs.fromBundle(it).track
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playerScreenState.observe(viewLifecycleOwner) {
            renderPlayerState(it)
        }
        viewModel.trackInfoState.observe(viewLifecycleOwner) {
            renderTrackInfo(it)
        }
        binding.topAppBar.setNavigationOnClickListener { findNavController().navigateUp() }
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