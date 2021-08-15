package dev.astler.unlib.view.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// L0R 1R 2R 3R 99R

class MarginHorizontalListDecorator(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        pView: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(pView) == 0) {
                left = spaceHeight
                right = spaceHeight
            } else {
                right = spaceHeight
            }

            top = spaceHeight
            bottom = spaceHeight
        }
    }
}
