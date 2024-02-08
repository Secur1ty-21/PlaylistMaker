package ru.yamost.playlistmaker

import ru.yamost.playlistmaker.create.di.createViewModelModule
import ru.yamost.playlistmaker.favorites.di.favoriteDataModule
import ru.yamost.playlistmaker.favorites.di.favoriteInteractorModule
import ru.yamost.playlistmaker.favorites.di.favoriteRepositoryModule
import ru.yamost.playlistmaker.favorites.di.favoritesViewModelModule
import ru.yamost.playlistmaker.player.di.playerDataModule
import ru.yamost.playlistmaker.player.di.playerInteractorModule
import ru.yamost.playlistmaker.player.di.playerViewModelModule
import ru.yamost.playlistmaker.playlist.di.playlistInteractorModule
import ru.yamost.playlistmaker.playlist.di.playlistRepositoryModule
import ru.yamost.playlistmaker.playlist.di.playlistViewModelModule
import ru.yamost.playlistmaker.playlistDetail.di.playlistDetailViewModelModule
import ru.yamost.playlistmaker.search.di.searchDataModule
import ru.yamost.playlistmaker.search.di.searchInteractorModule
import ru.yamost.playlistmaker.search.di.searchRepositoryModule
import ru.yamost.playlistmaker.search.di.searchViewModelModule
import ru.yamost.playlistmaker.settings.di.settingsInteractorModule
import ru.yamost.playlistmaker.settings.di.settingsRepositoryModule
import ru.yamost.playlistmaker.settings.di.settingsViewModelModule
import ru.yamost.playlistmaker.sharing.di.sharingDataModule
import ru.yamost.playlistmaker.sharing.di.sharingInteractorModule
import ru.yamost.playlistmaker.sharing.di.sharingRepositoryModule

object DiModuleProvider {
    val searchModules = listOf(
        searchDataModule,
        searchRepositoryModule,
        searchInteractorModule,
        searchViewModelModule
    )
    val playerModules = listOf(
        playerDataModule,
        playerInteractorModule,
        playerViewModelModule
    )
    val settingsModules = listOf(
        settingsRepositoryModule,
        settingsInteractorModule,
        settingsViewModelModule
    )
    val sharingModules = listOf(
        sharingDataModule,
        sharingRepositoryModule,
        sharingInteractorModule
    )
    val favoritesModules = listOf(
        favoriteDataModule,
        favoriteRepositoryModule,
        favoriteInteractorModule,
        favoritesViewModelModule
    )
    val playlistModules = listOf(
        playlistRepositoryModule,
        playlistInteractorModule,
        playlistViewModelModule
    )
    val createModules = listOf(
        createViewModelModule
    )
    val playlistDetailModules = listOf(
        playlistDetailViewModelModule
    )
}