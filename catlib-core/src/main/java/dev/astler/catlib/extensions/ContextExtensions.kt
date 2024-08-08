package dev.astler.catlib.extensions

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("DiscouragedApi")
fun Context.getStringIdByName(string: String): Int {
    val id = resources.getIdentifier(string, "string", packageName)
    return if (id != 0) id else gg.pressf.resources.R.string.nothing
}

fun Context.getStringByName(name: String, fallbackPrefix: String = "no:"): String {
    val id = getStringIdByName(name)
    return if (id != 0) getString(id) else fallbackPrefix + name
}