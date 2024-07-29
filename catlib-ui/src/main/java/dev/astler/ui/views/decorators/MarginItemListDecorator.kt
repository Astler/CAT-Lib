package dev.astler.ui.views.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemListDecorator(private val spaceHeight: Int, private val setSidePadding: Boolean = true) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceHeight
            }

            if (setSidePadding) {
                left = spaceHeight
                right = spaceHeight
            }

            bottom = spaceHeight
        }
    }
}
