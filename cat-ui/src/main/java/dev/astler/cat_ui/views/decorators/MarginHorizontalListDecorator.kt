package dev.astler.cat_ui.views.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

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
