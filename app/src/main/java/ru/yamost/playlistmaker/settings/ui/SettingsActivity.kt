package ru.yamost.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.yamost.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            owner = this, factory = SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
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
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }
    }
}