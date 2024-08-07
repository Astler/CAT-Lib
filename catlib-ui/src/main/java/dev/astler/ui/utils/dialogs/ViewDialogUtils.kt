package dev.astler.ui.utils.dialogs

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

fun View.okDialog(
    title: Any? = null,
    message: Any? = null,
    okAction: (DialogInterface) -> Unit = {}
): AlertDialog = context.okDialog(title = title, message = message, okAction = okAction)

fun View.clickDialog(
    title: Any? = null,
    message: Any? = null,
    okAction: (DialogInterface) -> Unit = {}
) {
    setOnClickListener {
        context.okDialog(title = title, message = message, okAction = okAction)
    }
}