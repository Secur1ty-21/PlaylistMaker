package ru.yamost.playlistmaker.settings.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yamost.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.isDarkTheme.observe(this) {
            binding.switchTheme.isChecked = it
        }
        setListeners()
    }

    private fun setListeners() {
        binding.buttonSharing.setOnClickListener {
            viewModel.shareApp()
        }
        binding.buttonSupport.setOnClickListener {
            viewModel.openSupport()
        }
        binding.buttonAgreement.setOnClickListener {
            viewModel.openTerms()
        }
        binding.topAppBar.setNavigationOnClickListener { finish() }
        binding.switchTheme.setOnClickListener {
            viewModel.switchTheme(binding.switchTheme.isChecked)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.onSystemUiModeChangedEvent(newConfig.uiMode)
        recreate()
    }
}