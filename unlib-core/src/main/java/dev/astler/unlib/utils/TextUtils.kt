package dev.astler.unlib.utils

import android.text.Html
import android.text.Spanned
import androidx.core.text.toSpannable

@Suppress("deprecation")
fun String?.fromHtml(): Spanned {
    if (this.isNullOrEmpty())
        return "null".toSpannable()

    return if (isN()) Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    else Html.fromHtml(this)
}
