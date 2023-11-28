package ru.yamost.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.yamost.playlistmaker.databinding.ActivityMainBinding
import ru.yamost.playlistmaker.media.ui.MediaActivity
import ru.yamost.playlistmaker.search.ui.SearchActivity
import ru.yamost.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonSearch.setOnClickListener {
            startImplicitIntent(SearchActivity::class.java)
        }
        binding.buttonMedia.setOnClickListener {
            startImplicitIntent(MediaActivity::class.java)
        }
        binding.buttonSettings.setOnClickListener {
            startImplicitIntent(SettingsActivity::class.java)
        }
    }

    private fun <T> startImplicitIntent(cls: Class<T>) {
        val intent = Intent(this@MainActivity, cls)
        startActivity(intent)
    }
}