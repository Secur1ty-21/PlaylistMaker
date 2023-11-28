package ru.yamost.playlistmaker.player.di

import android.os.Handler
import android.os.Looper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yamost.playlistmaker.player.ui.PlayerViewModel
import ru.yamost.playlistmaker.search.domain.model.Track

val playerViewModelModule = module {
    viewModel { (track: Track?) ->
        PlayerViewModel(
            track = track,
            interactor = get(),
            handler = get()
        )
    }
    factory {
        Handler(Looper.getMainLooper())
    }
}