package dev.astler.unli.view.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoratorAsym(private val spaceHeight: Int, private val totalItemsInLine: Int, private val adsFirst: Int, private val adsStep: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == adsFirst || (parent.getChildAdapterPosition(view) - adsFirst) % adsStep == 0) {

        }
        else {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) < totalItemsInLine) {
                    top = spaceHeight
                }

                if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) % 2 == 0)
                    if (parent.getChildAdapterPosition(view) < adsFirst && parent.getChildAdapterPosition(view) < (adsFirst + adsStep * ((parent.getChildAdapterPosition(view) - adsFirst) % adsStep)))
                        left = spaceHeight
                    else {
                        right = spaceHeight
                    }

                bottom = spaceHeight
            }
        }

    }
}