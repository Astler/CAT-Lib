package dev.astler.unli.utils

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import dev.astler.unli.R

fun Context.showInfoDialog(title: String, msg: CharSequence): AlertDialog {
    val dialog = AlertDialog.Builder(this)

    dialog.setPositiveButton(R.string.ok) { _, _ -> }

    dialog.setTitle(title)
    dialog.setMessage(msg)

    return dialog.create()
}

fun Context.showInfoMsgDialog(msg: CharSequence) {
    showInfoDialog("", msg).show()
}

fun Context.showInfoDialog(@StringRes title: Int, @StringRes msg: Int): AlertDialog {
    return this.showInfoDialog(getString(title), getString(msg))
}

fun Activity.exitDialog(

) {
    val dialog = AlertDialog.Builder(this)
//        .setTitle(R.string.exiting_app)
//        .setMessage(R.string.already_leave)
//        .setPositiveButton(R.string.yes) { _, _ ->
//            Utils.vibrateOnClick(activity)
//            this.finish() }
//        .setNegativeButton(R.string.no) { _, _ -> Utils.vibrateOnClick(activity)}.create()

    dialog.show()
}