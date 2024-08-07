package dev.astler.ui.utils.views

import android.widget.Button
import dev.astler.catlib.helpers.openWebUrl

fun Button.setLink(pLink: String) {
    setOnClickListener {
        context.openWebUrl(pLink)
    }
}
