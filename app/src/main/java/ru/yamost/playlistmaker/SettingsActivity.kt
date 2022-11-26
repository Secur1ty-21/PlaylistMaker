package ru.yamost.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonSharing = findViewById<Button>(R.id.button_sharing)
        val buttonSupport = findViewById<Button>(R.id.button_support)
        val buttonAgreement = findViewById<Button>(R.id.button_agreement)
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        buttonSharing.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_share))
            startActivity(intent)
        }
        buttonSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text))
            startActivity(intent)
        }
        buttonAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.url_agreement))
            startActivity(intent)
        }
        topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}