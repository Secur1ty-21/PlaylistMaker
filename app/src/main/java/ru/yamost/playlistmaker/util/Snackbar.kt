package ru.yamost.playlistmaker.util

import android.view.View
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import ru.yamost.playlistmaker.R

object Snackbar {
    fun show(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).setTextColor(
            MaterialColors.getColor(
                view, R.attr.playlistMakerButtonTextColor
            )
        ).setBackgroundTint(
            MaterialColors.getColor(
                view, R.attr.playlistMakerSnackbarTint
            )
        ).show()
    }
}