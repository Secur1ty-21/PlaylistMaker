package ru.yamost.playlistmaker.sharing.data

import android.content.Context
import ru.yamost.playlistmaker.R
import ru.yamost.playlistmaker.sharing.domain.api.SharingStringResRepository
import ru.yamost.playlistmaker.sharing.domain.model.EmailData

class SharingStringResRepositoryImpl(
    context: Context
) : SharingStringResRepository {
    override val appLink = context.getString(R.string.url_share)
    override val termsLink = context.getString(R.string.url_agreement)
    override val supportEmailData = EmailData(
        email = context.getString(R.string.support_email),
        title = context.getString(R.string.support_email_subject),
        message = context.getString(R.string.support_email_text)
    )
}