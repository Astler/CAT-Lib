package dev.astler.cat_ui.utils.dialogs

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

fun View.okDialog(
    title: Any? = null,
    message: Any? = null,
    okAction: (DialogInterface) -> Unit = {}
): AlertDialog = context.okDialog(title, message, okAction)

fun View.clickDialog(
    title: Any? = null,
    message: Any? = null,
    okAction: (DialogInterface) -> Unit = {}
) {
    setOnClickListener {
        context.okDialog(title, message, okAction)
    }
}