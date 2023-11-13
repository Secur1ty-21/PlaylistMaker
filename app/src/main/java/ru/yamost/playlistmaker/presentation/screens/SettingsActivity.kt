package ru.yamost.playlistmaker.presentation.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.yamost.playlistmaker.App
import ru.yamost.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var buttonSharing: Button
    private lateinit var buttonSupport: Button
    private lateinit var buttonAgreement: Button
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var switchTheme: SwitchMaterial
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initViews()
        setListeners()
    }

    private fun initViews() {
        buttonSharing = findViewById(R.id.button_sharing)
        buttonSupport = findViewById(R.id.button_support)
        buttonAgreement = findViewById(R.id.button_agreement)
        topAppBar = findViewById(R.id.topAppBar)
        switchTheme = findViewById(R.id.switchTheme)
        switchTheme.isChecked = App.isDarkTheme
    }

    private fun setListeners() {
        buttonSharing.setOnClickListener {
            onClickButtonSharing()
        }
        buttonSupport.setOnClickListener {
            onClickButtonSupport()
        }
        buttonAgreement.setOnClickListener {
            onClickButtonAgreement()
        }
        topAppBar.setNavigationOnClickListener { finish() }
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }
    }

    private fun onClickButtonSharing() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_share))
        startImplicitIntent(intent)
    }

    private fun onClickButtonSupport() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text))
        startImplicitIntent(intent)
    }

    private fun onClickButtonAgreement() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.url_agreement))
        startImplicitIntent(intent)
    }

    private fun startImplicitIntent(intent: Intent) {
        if (isIntentSafe(intent)) {
            startActivity(intent)
        }
    }

    private fun isIntentSafe(intent: Intent): Boolean {
        val activities = packageManager.queryIntentActivities(intent, 0)
        return activities.size > 0
    }

    override fun onStop() {
        super.onStop()
        (applicationContext as App).savePreference()
    }
}