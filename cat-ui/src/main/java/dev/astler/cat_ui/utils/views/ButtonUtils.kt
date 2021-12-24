package dev.astler.cat_ui.utils.views

import android.widget.Button
import dev.astler.unlib.utils.openWebUrl

fun Button.setLink(pLink: String) {
    setOnClickListener {
        context.openWebUrl(pLink)
    }
}
