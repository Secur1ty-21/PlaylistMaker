package ru.yamost.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.core.ui.PlaylistAdapter
import ru.yamost.playlistmaker.databinding.FragmentPlaylistBinding
import ru.yamost.playlistmaker.media.ui.MediaFragmentDirections
import ru.yamost.playlistmaker.playlist.ui.model.PlaylistScreenState

class PlaylistFragment : Fragment() {
    private val viewModel by viewModel<PlaylistViewModel>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding get() = _binding!!
    private val playlistAdapter = PlaylistAdapter {
        val action = MediaFragmentDirections.actionMediaFragmentToPlaylistDetailFragment(it.id)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreatePlaylist.setOnClickListener {
            val action = MediaFragmentDirections.actionMediaFragmentToCreateFragment()
            findNavController().navigate(action)
        }
        binding.rvPlaylist.adapter = playlistAdapter
        binding.rvPlaylist.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvPlaylist.addItemDecoration(
            PlaylistItemDecorator(
                resources.getDimensionPixelSize(R.dimen.playlist_item_between_space)
            )
        )
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistScreenState) {
        binding.progress.hide()
        when (state) {
            is PlaylistScreenState.Content -> {
                binding.imgNotCreated.isVisible = false
                binding.lblNotCreated.isVisible = false
                playlistAdapter.updateContent(state.playlistList)
            }

            is PlaylistScreenState.Empty -> {
                playlistAdapter.updateContent(emptyList())
                binding.imgNotCreated.isVisible = true
                binding.lblNotCreated.isVisible = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylistList()
        binding.progress.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}