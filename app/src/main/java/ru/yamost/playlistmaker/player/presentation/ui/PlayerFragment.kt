package ru.yamost.playlistmaker.player.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.core.ui.PlaylistAdapter
import ru.yamost.playlistmaker.create.presentation.ui.CreateFragment
import ru.yamost.playlistmaker.databinding.FragmentPlayerBinding
import ru.yamost.playlistmaker.player.presentation.PlayerViewModel
import ru.yamost.playlistmaker.player.presentation.model.PlayerAction
import ru.yamost.playlistmaker.player.presentation.model.PlayerScreenState
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.util.Snackbar

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = _binding!!
    private var track: Track? = null
    private val viewModel by viewModel<PlayerViewModel> { parametersOf(track) }
    private val playlistListAdapter = PlaylistAdapter(isLinearType = true) { playlist ->
        viewModel.onPlaylistItemClickEvent(playlist)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { track = PlayerFragmentArgs.fromBundle(it).track }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playerScreenState.observe(viewLifecycleOwner) { renderPlayerState(it) }
        viewModel.trackInfoState.observe(viewLifecycleOwner) { renderTrackInfo(it) }
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.addToFavoriteButton.setImageResource(R.drawable.ic_favorite_active)
            } else {
                binding.addToFavoriteButton.setImageResource(R.drawable.ic_favorite_inactive)
            }
        }
        viewModel.observePlaylistListState().observe(viewLifecycleOwner) { playlistList ->
            playlistListAdapter.updateContent(playlistList)
        }
        binding.topAppBar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.playButton.setOnClickListener { viewModel.onPlayButton() }
        binding.addToFavoriteButton.setOnClickListener { viewModel.onClickFavoriteBtn() }
        binding.rvPlaylistList.adapter = playlistListAdapter
        binding.rvPlaylistList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        viewModel.observeAction().observe(viewLifecycleOwner) { action ->
            action?.let {
                if (it is PlayerAction.ShowSnackbar && it.isTrackAdded) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
                renderAction(it)
            }
        }
        binding.btnCreatePlaylist.setOnClickListener {
            val action = PlayerFragmentDirections.actionPlayerFragmentToCreateFragment()
            findNavController().navigate(action)
        }
        setFragmentResultListener(
            requestKey = CreateFragment.RESULT_KEY_CREATE_SUCCESS
        ) { _, bundle ->
            viewModel.updatePlaylistList()
            showSnackbar(
                getString(
                    R.string.create_playlist_created_msg,
                    bundle.getString(CreateFragment.KEY_NAME_CREATED_PLAYLIST)
                )
            )
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

    private fun renderAction(action: PlayerAction) {
        if (action is PlayerAction.ShowSnackbar) {
            if (action.isTrackAdded) {
                showSnackbar(
                    getString(
                        R.string.player_msg_track_added, action.playlistName
                    )
                )
            } else {
                showSnackbar(
                    getString(
                        R.string.player_msg_track_exist_in_playlist, action.playlistName
                    )
                )
            }
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.show(binding.root, text)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}