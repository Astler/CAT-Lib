package dev.astler.unlib.view.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginGridDecorator(private val spaceHeight: Int, private val totalItemsInLine: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {

        with(outRect) {
            if (parent.getChildAdapterPosition(view) < totalItemsInLine) {
                top = spaceHeight
            }

            if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) % totalItemsInLine == 0) {
                left = spaceHeight
                right = spaceHeight
            } else {
                right = spaceHeight
            }

            bottom = spaceHeight
        }

    }
}