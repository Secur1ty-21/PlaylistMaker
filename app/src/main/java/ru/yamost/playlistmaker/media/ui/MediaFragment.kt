package ru.yamost.playlistmaker.media.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.tabs.TabLayoutMediator
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.create.presentation.ui.CreateFragment
import ru.yamost.playlistmaker.databinding.FragmentMediaBinding
import ru.yamost.playlistmaker.util.Snackbar

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding: FragmentMediaBinding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener { requireActivity().finish() }
        binding.viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.media_tab_favorites)
                1 -> tab.text = getString(R.string.media_tab_playlists)
            }
        }
        tabMediator.attach()
        setFragmentResultListener(
            requestKey = CreateFragment.RESULT_KEY_CREATE_SUCCESS
        ) { _, bundle ->
            Snackbar.show(
                binding.root,
                getString(
                    R.string.create_playlist_created_msg,
                    bundle.getString(CreateFragment.KEY_NAME_CREATED_PLAYLIST)
                ),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}