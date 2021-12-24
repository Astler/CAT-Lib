package dev.astler.cat_ui.views.span

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class CustomFancyTextSpan(private val mTypeface: Typeface?, private val mColor: Int = -1) : MetricAffectingSpan() {
    override fun updateDrawState(paint: TextPaint) {
        if (mTypeface != null) paint.typeface = mTypeface
        if (mColor != -1) paint.color = mColor
    }

    override fun updateMeasureState(paint: TextPaint) {
        if (mTypeface != null) paint.typeface = mTypeface
        if (mColor != -1) paint.color = mColor
    }
}
