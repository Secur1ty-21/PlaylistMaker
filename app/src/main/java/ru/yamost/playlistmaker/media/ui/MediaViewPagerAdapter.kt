package ru.yamost.playlistmaker.media.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.yamost.playlistmaker.favorites.ui.FavoritesFragment
import ru.yamost.playlistmaker.playlist.ui.PlaylistFragment

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}