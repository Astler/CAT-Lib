package dev.astler.unlib.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import dev.astler.unlib.core.R

fun Context.showInfoDialog(pTitle: CharSequence, pMsg: CharSequence) {
    unLibDialog(pTitle, pMsg, getString(R.string.ok), pPositiveClick = { })
}

fun Context.showInfoMsgDialog(pMsg: CharSequence) {
    unLibDialog("", pMsg, getString(R.string.ok), pPositiveClick = { })
}

fun Context.showInfoDialog(@StringRes title: Int, @StringRes msg: Int) {
    this.showInfoDialog(getString(title), getString(msg))
}

fun Activity.exitDialog() {
    confirmDialog(R.string.exiting_app, getString(R.string.already_leave), getString(R.string.yes), getString(R.string.no), {
        this.vibrateOnClick()
        this.finish()
    })
}

fun Context.confirmDialog(
        @StringRes pTitle: Int,
        pMsg: CharSequence,
        pPositiveText: CharSequence,
        pNegativeText: CharSequence,
        pPositiveClick: ((pDialogInterface: DialogInterface) -> Unit),
        pNegativeClick: ((pDialogInterface: DialogInterface) -> Unit) = {}
) {
    unLibDialog(getString(pTitle), pMsg, pPositiveText, pNegativeText, pPositiveClick, pNegativeClick)
}

fun Context.confirmDialog(
        @StringRes pTitle: Int,
        pMsg: CharSequence,
        @StringRes pPositiveText: Int,
        @StringRes pNegativeText: Int,
        pPositiveClick: ((pDialogInterface: DialogInterface) -> Unit),
        pNegativeClick: ((pDialogInterface: DialogInterface) -> Unit)
) {
    unLibDialog(getString(pTitle), pMsg, getString(pPositiveText), getString(pNegativeText), pPositiveClick, pNegativeClick)
}

fun Context.unLibDialog(
        @StringRes pTitle: Int,
        pMsg: CharSequence,
        pPositiveText: CharSequence,
        pNegativeText: CharSequence,
        pPositiveClick: ((pDialogInterface: DialogInterface) -> Unit)? = null,
        pNegativeClick: ((pDialogInterface: DialogInterface) -> Unit)? = null
) {
    unLibDialog(getString(pTitle), pMsg, pPositiveText, pNegativeText, pPositiveClick, pNegativeClick)
}

fun Context.unLibDialog(
        @StringRes pTitle: Int,
        pMsg: CharSequence,
        @StringRes pPositiveText: Int,
        @StringRes pNegativeText: Int,
        pPositiveClick: ((pDialogInterface: DialogInterface) -> Unit)? = null,
        pNegativeClick: ((pDialogInterface: DialogInterface) -> Unit)? = null
) {
    unLibDialog(getString(pTitle), pMsg, getString(pPositiveText), getString(pNegativeText), pPositiveClick, pNegativeClick)
}

fun Context.unLibDialog(
        pTitle: CharSequence,
        pMsg: CharSequence,
        pPositiveText: CharSequence = "",
        pNegativeText: CharSequence = "",
        pPositiveClick: ((pDialogInterface: DialogInterface) -> Unit)? = null,
        pNegativeClick: ((pDialogInterface: DialogInterface) -> Unit)? = null
) {
    val nDialog = AlertDialog.Builder(this)
    nDialog.setTitle(pTitle)
    nDialog.setMessage(pMsg)

    if (pPositiveClick != null) {
        nDialog.setPositiveButton(pPositiveText) { pDialog, _ ->
            pPositiveClick(pDialog)
        }
    }

    if (pNegativeClick != null) {
        nDialog.setNegativeButton(pNegativeText) { pDialog, _ ->
            pNegativeClick(pDialog)
        }
    }

    nDialog.create().show()
}