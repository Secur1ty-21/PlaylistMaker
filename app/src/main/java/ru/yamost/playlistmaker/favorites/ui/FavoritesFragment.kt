package ru.yamost.playlistmaker.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yamost.playlistmaker.databinding.FragmentFavoritesBinding
import ru.yamost.playlistmaker.media.ui.MediaFragmentDirections
import ru.yamost.playlistmaker.search.domain.model.Track
import ru.yamost.playlistmaker.search.ui.TrackListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding get() = _binding!!
    private val viewModel by viewModel<FavoritesViewModel>()
    private lateinit var adapter: TrackListAdapter
    private val trackList = mutableListOf<Track>()
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TrackListAdapter(trackList)
        adapter.itemClickListener = { onTrackClick(it) }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.observeScreenState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeIsClickDebounce().observe(viewLifecycleOwner) {
            isClickAllowed = it
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFavoriteTracks()
    }

    private fun render(state: FavoriteScreenState) {
        when (state) {
            is FavoriteScreenState.Empty -> {
                binding.progress.hide()
                binding.emptyBlock.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }

            is FavoriteScreenState.Loading -> {
                binding.progress.show()
                binding.emptyBlock.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
            }

            is FavoriteScreenState.Content -> {
                binding.progress.hide()
                binding.emptyBlock.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                updateRecyclerView(state.trackList)
            }
        }
    }

    private fun onTrackClick(track: Track) {
        if (debounceClick()) {
            val action = MediaFragmentDirections.actionMediaFragmentToPlayerFragment(track)
            findNavController().navigate(action)
        }
    }

    private fun debounceClick(): Boolean {
        val current = isClickAllowed
        if (current) {
            isClickAllowed = false
            viewModel.debounceClick()
        }
        return current
    }

    private fun updateRecyclerView(newList: List<Track>) {
        val prevSize = trackList.size
        val newSize = newList.size
        if (prevSize == newSize) {
            newList.forEachIndexed { index, track ->
                if (trackList[index].id != track.id) {
                    trackList[index] = track
                    adapter.notifyItemChanged(index)
                }
            }
        } else if (prevSize < newSize) {
            for (i in 0 until prevSize) {
                if (trackList[i].id != newList[i].id) {
                    trackList[i] = newList[i]
                    adapter.notifyItemChanged(i)
                }
            }
            trackList.addAll(newList.slice(prevSize until newSize))
            adapter.notifyItemRangeInserted(prevSize, newSize - prevSize)
        } else {
            for (i in 0 until newSize) {
                if (trackList[i].id != newList[i].id) {
                    trackList[i] = newList[i]
                    adapter.notifyItemChanged(i)
                }
            }
            for (i in newSize until prevSize) {
                trackList.removeAt(i)
                adapter.notifyItemRemoved(i)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}