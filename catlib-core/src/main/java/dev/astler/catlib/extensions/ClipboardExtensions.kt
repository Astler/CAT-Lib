package dev.astler.catlib.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import dev.astler.catlib.core.R

fun Context.copyToBuffer(dataToCopy: CharSequence) {
    if (dataToCopy.isEmpty()) return

    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val newClip = ClipData.newPlainText("text", dataToCopy)
    clipboardManager.setPrimaryClip(newClip)

    toast(getString(R.string.copied_to_buffer, dataToCopy))
}

fun Context.readFromBuffer(): CharSequence? {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clipboardManager.primaryClip?.getItemAt(0)?.text
}