package ru.yamost.playlistmaker.playlist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val playlistAdapter = PlaylistAdapter()

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
        binding.rvPlaylist.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvPlaylist.addItemDecoration(PlaylistItemDecorator(
            resources.getDimensionPixelSize(R.dimen.playlist_item_between_space)
        ))
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistScreenState) {
        when (state) {
            is PlaylistScreenState.Content -> {
                binding.imgNotCreated.visibility = View.GONE
                binding.lblNotCreated.visibility = View.GONE
                playlistAdapter.updateContent(state.playlistList)
            }

            is PlaylistScreenState.Empty -> {
                playlistAdapter.updateContent(emptyList())
                binding.imgNotCreated.visibility = View.VISIBLE
                binding.lblNotCreated.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.v(PlaylistFragment::class.java.simpleName, "onResume")
        viewModel.updatePlaylistList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}