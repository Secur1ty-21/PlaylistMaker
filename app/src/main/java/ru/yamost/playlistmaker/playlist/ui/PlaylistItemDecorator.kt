package ru.yamost.playlistmaker.playlist.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PlaylistItemDecorator(
    private val betweenItemsOffsetPx: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val isSecondColumn = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition % 2 == 1
        if (isSecondColumn) {
            outRect.set(betweenItemsOffsetPx / 2, 0, 0, 0)
        } else {
            outRect.set(0, 0, betweenItemsOffsetPx / 2, 0)
        }
    }
}