package dev.astler.cat_ui.utils.views

import android.widget.TextView
import androidx.annotation.StringRes

fun TextView.safeSetText(@StringRes pResId: Int, pHideIfEmpty: Boolean = true) {
    if (pResId == -1) {
        if (pHideIfEmpty)
            gone()
        else
            text = ""
        return
    } else {
        setText(pResId)
        visible()
    }
}

fun TextView.safeSetText(pText: String?, pHideIfEmpty: Boolean = true) {
    if (pText.isNullOrEmpty()) {
        if (pHideIfEmpty)
            gone()
        else
            text = ""
        return
    } else {
        text = pText
        visible()
    }
}
