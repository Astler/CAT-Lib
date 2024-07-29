package dev.astler.ui.views.transformers

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import dev.astler.ui.R
import kotlin.math.abs

class CatPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val resources = page.resources
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

        page.translationX = -pageTranslationX * position
        page.scaleY = 1 - (0.25f * abs(position))
        page.scaleX = 1 - (0.25f * abs(position))
        page.alpha = 1 - abs(position)
    }
}
