package ru.yamost.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import ru.yamost.playlistmaker.adapter.TrackListAdapter
import ru.yamost.playlistmaker.data.cache.TracksDataStore
import ru.yamost.playlistmaker.data.model.Track
import ru.yamost.playlistmaker.data.network.ResponseTrackList
import ru.yamost.playlistmaker.data.network.ResultCallback

@SuppressLint("NotifyDataSetChanged")
class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_INPUT_TEXT = "SEARCH_INPUT_TEXT"
    }

    private var searchInputText = ""
    private val tracksList = mutableListOf<Track>()
    private val trackListAdapter = TrackListAdapter(tracksList)
    private lateinit var tracksRecycler: RecyclerView
    private lateinit var errorBlock: LinearLayout
    private lateinit var errorMessage: TextView
    private lateinit var errorIcon: ImageView
    private lateinit var refreshButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var progressBar: ProgressBar
    private var requestGetTracksBySearchQuery: Call<ResponseTrackList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val clearButton = findViewById<ImageButton>(R.id.button_clear)
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        searchEditText = findViewById(R.id.search_text)
        refreshButton = findViewById(R.id.refresh_button)
        errorBlock = findViewById(R.id.block_error)
        errorMessage = findViewById(R.id.message_error)
        errorIcon = findViewById(R.id.icon_error)
        progressBar = findViewById(R.id.progress_bar)
        tracksRecycler = findViewById(R.id.track_list)
        tracksRecycler.adapter = trackListAdapter

        val textListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    searchInputText = s.toString()
                    clearButton.visibility = View.VISIBLE
                }
            }
        }
        searchEditText.addTextChangedListener(textListener)
        topAppBar.setNavigationOnClickListener { finish() }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            onSearchEditorAction(actionId, searchEditText)
        }
        clearButton.setOnClickListener { onClickClearButton() }
        refreshButton.setOnClickListener { updateTrackListBySearchQuery(searchInputText) }
    }

    private fun onSearchEditorAction(actionId: Int, view: View): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            closeKeyboard(view)
            updateTrackListBySearchQuery(searchInputText)
            return true
        }
        return false
    }

    private fun closeKeyboard(view: View) {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun onClickClearButton() {
        searchEditText.setText("")
        searchInputText = ""
        tracksList.clear()
        trackListAdapter.notifyDataSetChanged()
        closeKeyboard(searchEditText)
    }

    private fun updateTrackListBySearchQuery(searchQuery: String) {
        tracksList.clear()
        trackListAdapter.notifyDataSetChanged()
        progressBar.visibility = ProgressBar.VISIBLE
        requestGetTracksBySearchQuery =
            TracksDataStore.getTracksBySearchQuery(searchQuery = searchQuery,
                resultCallback = object : ResultCallback<List<Track>> {
                    override fun onSuccess(data: List<Track>) {
                        refreshButton.visibility = Button.INVISIBLE
                        progressBar.visibility = ProgressBar.INVISIBLE
                        if (data.isEmpty()) {
                            showError(R.drawable.ic_not_found, R.string.search_not_found)
                        } else {
                            tracksList.addAll(data)
                            trackListAdapter.notifyDataSetChanged()
                            errorBlock.visibility = LinearLayout.INVISIBLE
                        }
                    }

                    override fun onFailure(error: Throwable) {
                        Log.d(
                            "RetrofitTag",
                            "${error.localizedMessage}\n${error.message}\n${error.stackTraceToString()}"
                        )
                        progressBar.visibility = ProgressBar.INVISIBLE
                        showError(R.drawable.ic_no_connection, R.string.search_no_connection)
                        refreshButton.visibility = Button.VISIBLE
                    }
                })
    }

    private fun showError(@DrawableRes iconId: Int, @StringRes textId: Int) {
        errorBlock.visibility = View.VISIBLE
        errorIcon.setImageResource(iconId)
        errorMessage.text = getString(textId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.search_text).setText(
            savedInstanceState.getString(
                SEARCH_INPUT_TEXT, ""
            )
        )
        if (searchInputText.isNotEmpty()) {
            updateTrackListBySearchQuery(searchInputText)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT_TEXT, searchInputText)
    }

    override fun onDestroy() {
        super.onDestroy()
        requestGetTracksBySearchQuery?.cancel()
    }
}