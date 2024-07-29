package dev.astler.ui.views.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginGridWithAdsDecorator(private val spaceHeight: Int, private val totalItemsInLine: Int, private val adsFirst: Int, private val adsStep: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == adsFirst || (parent.getChildAdapterPosition(view) - adsFirst) % adsStep == 0) {
            // do nothing!
        } else {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) < totalItemsInLine) {
                    top = spaceHeight
                }

                if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) % 2 == 0) {

                    if (parent.getChildAdapterPosition(view) < adsFirst)
                        left = spaceHeight
                    else {
                        if (((parent.getChildAdapterPosition(view) - adsFirst) % adsStep) % 2 == 0)
                            right = spaceHeight
                        else {
                            left = spaceHeight
                        }
                    }
                } else {
                    left = spaceHeight
                    right = spaceHeight
                }

                bottom = spaceHeight
            }
        }
    }
}
