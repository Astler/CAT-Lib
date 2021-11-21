package dev.astler.unlib.utils.views

import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import dev.astler.unlib.utils.dialog
import dev.astler.unlib.utils.okDialog

fun View.okDialog(
    pTitle: String = "",
    pMsg: String = "",
    pOkAction: () -> Unit = {},
    pInstantDialog: Boolean = true
): AlertDialog = context.okDialog(pTitle, pMsg, pOkAction, pInstantDialog)

fun View.clickDialog(@StringRes pDialogMsgId: Int) {
    setOnClickListener {
        context.dialog(pMsg = context.getString(pDialogMsgId))
    }
}

fun View.clickDialog(pDialogMsg: String) {
    setOnClickListener {
        context.dialog(pMsg = pDialogMsg)
    }
}
