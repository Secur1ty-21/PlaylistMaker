package ru.yamost.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.yamost.playlistmaker.sharing.domain.api.ExternalNavigator
import ru.yamost.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(
    private val context: Context
) : ExternalNavigator {
    override fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link)
        startImplicitIntent(intent)
    }

    override fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        startImplicitIntent(intent)
    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
        intent.putExtra(Intent.EXTRA_SUBJECT, emailData.title)
        intent.putExtra(Intent.EXTRA_TEXT, emailData.message)
        startImplicitIntent(intent)
    }

    private fun startImplicitIntent(intent: Intent) {
        if (isIntentSafe(intent)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    private fun isIntentSafe(intent: Intent): Boolean {
        val activities = context.packageManager.queryIntentActivities(intent, 0)
        return activities.size > 0
    }
}