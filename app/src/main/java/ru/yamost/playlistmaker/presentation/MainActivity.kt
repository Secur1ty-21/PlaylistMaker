package ru.yamost.playlistmaker.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ru.yamost.playlistmaker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMedia = findViewById<Button>(R.id.button_media)
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSearch.setOnClickListener {
            startImplicitIntent(SearchActivity::class.java)
        }
        buttonMedia.setOnClickListener {
            startImplicitIntent(MediaActivity::class.java)
        }
        buttonSettings.setOnClickListener {
            startImplicitIntent(SettingsActivity::class.java)
        }
    }

    private fun <T> startImplicitIntent(cls: Class<T>) {
        val intent = Intent(this@MainActivity, cls)
        startActivity(intent)
    }
}