package ru.yamost.playlistmaker.playlistDetail.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.core.ui.TrackAdapter
import ru.yamost.playlistmaker.create.presentation.ui.CreateFragment
import ru.yamost.playlistmaker.databinding.FragmentPlaylistDetailBinding
import ru.yamost.playlistmaker.playlist.domain.model.PlaylistWithTracks
import ru.yamost.playlistmaker.playlistDetail.presentation.PlaylistDetailViewModel
import ru.yamost.playlistmaker.playlistDetail.presentation.model.PlaylistDetailAction
import ru.yamost.playlistmaker.util.Snackbar

class PlaylistDetailFragment : Fragment() {
    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding: FragmentPlaylistDetailBinding get() = _binding!!
    private var playlistId: Int? = null
    private val tracksAdapter = TrackAdapter(
        onItemClick = {
            val action =
                PlaylistDetailFragmentDirections.actionPlaylistDetailFragmentToPlayerFragment(it)
            findNavController().navigate(action)
        },
        onItemLongClick = { track ->
            MaterialAlertDialogBuilder(requireContext())
                .setPositiveButton(R.string.playlist_detail_dlg_btn_delelte) { _, _ -> viewModel.onDialogBtnDeleteTrack(track) }
                .setNegativeButton(R.string.playlist_detail_dlg_btn_cancel, null)
                .setTitle(R.string.playlist_detail_dlg_title)
                .setMessage(R.string.playlist_detail_dlg_message)
                .show()
            true
        }
    )
    private val viewModel by viewModel<PlaylistDetailViewModel> { parametersOf(playlistId!!) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { playlistId = PlaylistDetailFragmentArgs.fromBundle(it).playlistId }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            isHideable = false
        }
        val moreActionBottomSheetBehavior = BottomSheetBehavior.from(binding.layoutMoreContent).apply {
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        viewModel.state.observe(viewLifecycleOwner) { render(it) }
        viewModel.action.observe(viewLifecycleOwner) {
            it?.let { action -> obtainAction(action) }
        }
        viewModel.isMoreBottomSheetVisible.observe(viewLifecycleOwner) {
            if (it) {
                moreActionBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        moreActionBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    viewModel.onCloseMoreBottomSheet()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        })
        binding.rvTrackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTrackList.adapter = tracksAdapter
        binding.topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.btnShare.setOnClickListener { viewModel.onBtnShareClick() }
        binding.btnMore.setOnClickListener { viewModel.onBtnMoreClick() }
        binding.btnMoreSharePlaylist.setOnClickListener { viewModel.onBtnShareClick() }
        binding.btnMoreDeletePlaylist.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setPositiveButton(R.string.playlist_detail_dlg_delete_playlist_pos_btn) { _, _ -> viewModel.onBtnDeletePlaylist() }
                .setNegativeButton(R.string.playlist_detail_dlg_delete_playlist_cancel_btn, null)
                .setTitle(R.string.playlist_detail_dlg_delete_playlist_title)
                .setMessage(getString(R.string.playlist_detail_dlg_delete_playlist_message, binding.lblName.text))
                .show()
        }
        binding.btnMoreEdit.setOnClickListener { viewModel.onBtnEditClick() }
        setFragmentResultListener(requestKey = CreateFragment.RESULT_KEY_CREATE_SUCCESS) { _, _ ->
            viewModel.updatePlaylist()
        }
    }

    private fun render(playlistWithTracks: PlaylistWithTracks) {
        with(playlistWithTracks) {
            if (playlist.imageUri == null) {
                binding.imgCover.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_track_placeholder
                    )
                )
                binding.playlistLinearItem.playlistCover.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_track_placeholder
                    )
                )
            } else {
                binding.imgCover.setImageURI(playlist.imageUri)
                binding.playlistLinearItem.playlistCover.setImageURI(playlist.imageUri)
            }
            binding.lblName.text = playlist.name
            binding.playlistLinearItem.lblPlaylistName.text = playlist.name
            binding.playlistLinearItem.lblPlaylistSize.text = resources.getQuantityString(
                R.plurals.playlist_item_size,
                playlist.size,
                playlist.size
            )
            binding.lblDetailInfo.text = getString(
                R.string.playlist_detail_info,
                resources.getQuantityString(
                    R.plurals.playlist_duration_minute,
                    playlistDuration.toInt(),
                    playlistDuration.toInt()
                ),
                resources.getQuantityString(
                    R.plurals.playlist_item_size, playlist.size, playlist.size
                )
            )
            if (playlist.description.isEmpty()) {
                binding.lblDescription.isVisible = false
            } else {
                binding.lblDescription.text = playlist.description
            }
            tracksAdapter.updateData(tracks)
        }
    }

    private fun obtainAction(action: PlaylistDetailAction) {
        when (action) {
            is PlaylistDetailAction.NavigateBack -> findNavController().popBackStack()
            is PlaylistDetailAction.ShowEmptyPlaylistMessage -> {
                Snackbar.show(binding.root, getString(R.string.playlist_detail_message_empty_playlist))
            }
            is PlaylistDetailAction.NavigateToEditScreen -> {
                val navAction = PlaylistDetailFragmentDirections.actionPlaylistDetailFragmentToCreateFragment(action.playlistId)
                findNavController().navigate(navAction)
            }

            is PlaylistDetailAction.UpdatePlaylist -> {
                action.playlist.imageUri?.let { uri ->
                    binding.imgCover.setImageURI(uri)
                }
                binding.lblName.text = action.playlist.name
                if (action.playlist.description.isNotEmpty()) {
                    binding.lblDescription.isVisible = true
                    binding.lblDescription.text = action.playlist.description
                } else {
                    binding.lblDescription.isVisible = false
                }
            }
        }
    }
}