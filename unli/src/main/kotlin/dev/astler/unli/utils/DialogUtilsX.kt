package dev.astler.unli.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import dev.astler.unli.R

fun Context.showInfoDialog(title: String, msg: String): AlertDialog {
    val dialog = AlertDialog.Builder(this)

    dialog.setPositiveButton(R.string.ok) { _, _ ->
    }

    dialog.setTitle(title)
    dialog.setMessage(msg)

    return dialog.create()
}

fun Context.showInfoDialog(@StringRes title: Int, @StringRes msg: Int): AlertDialog {
    return this.showInfoDialog(getString(title), getString(msg))
}
