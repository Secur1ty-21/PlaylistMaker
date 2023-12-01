package ru.yamost.playlistmaker.settings.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yamost.playlistmaker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isDarkTheme.observe(viewLifecycleOwner) {
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
        binding.topAppBar.setNavigationOnClickListener { requireActivity().finish() }
        binding.switchTheme.setOnClickListener {
            viewModel.switchTheme(binding.switchTheme.isChecked)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.onSystemUiModeChangedEvent(newConfig.uiMode)
        requireActivity().recreate()
    }
}