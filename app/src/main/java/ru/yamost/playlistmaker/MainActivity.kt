package ru.yamost.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMedia = findViewById<Button>(R.id.button_media)
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        val buttonSearchListener: View.OnClickListener = object: View.OnClickListener {
            override fun onClick(p0: View?) {
                startImplicitIntent(SearchActivity::class.java)
            }
        }
        buttonSearch.setOnClickListener(buttonSearchListener)
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