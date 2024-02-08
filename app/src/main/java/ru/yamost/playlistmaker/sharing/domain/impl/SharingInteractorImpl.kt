package ru.yamost.playlistmaker.sharing.domain.impl

import ru.yamost.playlistmaker.playlist.domain.model.PlaylistWithTracks
import ru.yamost.playlistmaker.sharing.domain.api.ExternalNavigator
import ru.yamost.playlistmaker.sharing.domain.api.SharingInteractor
import ru.yamost.playlistmaker.sharing.domain.api.SharingStringResRepository
import ru.yamost.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val stringResRepository: SharingStringResRepository
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return stringResRepository.appLink
    }

    private fun getSupportEmailData(): EmailData {
        return stringResRepository.supportEmailData
    }

    private fun getTermsLink(): String {
        return stringResRepository.termsLink
    }

    override fun sharePlaylist(playlistWithTracks: PlaylistWithTracks) {
        externalNavigator.sharePlaylist(stringResRepository.getPlaylistStringForShare(playlistWithTracks))
    }
}