package dev.astler.unlib.utils.views

import android.widget.TextView
import androidx.annotation.StringRes

fun TextView.safeSetText(@StringRes pResId: Int, pHideIfEmpty: Boolean = true) {
    if (pResId == -1) {
        if (pHideIfEmpty)
            goneView()
        else
            text = ""
        return
    } else {
        setText(pResId)
        showView()
    }
}

fun TextView.safeSetText(pText: String?, pHideIfEmpty: Boolean = true) {
    if (pText.isNullOrEmpty()) {
        if (pHideIfEmpty)
            goneView()
        else
            text = ""
        return
    } else {
        text = pText
        showView()
    }
}
