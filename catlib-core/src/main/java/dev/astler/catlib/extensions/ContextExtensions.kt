package dev.astler.catlib.extensions

import android.content.Context

fun Context.getStringByName(string: String): Int {
    val id = resources.getIdentifier(string, "string", packageName)
    return if (id != 0) id else gg.pressf.resources.R.string.nothing
}