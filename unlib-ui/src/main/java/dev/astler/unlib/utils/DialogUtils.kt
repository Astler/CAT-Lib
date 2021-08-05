package dev.astler.unlib.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import dev.astler.unlib.adapters.BaseOneItemListAdapter
import dev.astler.unlib.core.R
import dev.astler.unlib.ui.databinding.DialogChooseItemBinding

fun Context.dialog(
    pTitle: String = "",
    pMsg: String = "",
    pInstantDialog: Boolean = true
): AlertDialog {
    val nDialog = AlertDialog.Builder(this)

    if (pTitle.isNotEmpty())
        nDialog.setTitle(pTitle)

    if (pMsg.isNotEmpty())
        nDialog.setMessage(pMsg)

    val nCreatedDialog = nDialog.create()

    if (pInstantDialog)
        nCreatedDialog.show()

    return nCreatedDialog
}

fun Context.okDialog(
    pTitle: String = "",
    pMsg: String = "",
    pOkAction: () -> Unit,
    pInstantDialog: Boolean = true
): AlertDialog {
    return confirmDialog(
        pTitle,
        pMsg,
        pPositiveText = getString(R.string.ok),
        pPositiveAction = pOkAction,
        pInstantDialog = pInstantDialog
    )
}

fun Context.confirmDialog(
    pTitle: String = "",
    pMsg: String = "",
    pPositiveText: String = "",
    pNegativeText: String = "",
    pPositiveAction: (() -> Unit)? = null,
    pNegativeAction: (() -> Unit)? = null,
    pInstantDialog: Boolean = true
): AlertDialog {
    val nDialog = AlertDialog.Builder(this)

    if (pTitle.isNotEmpty())
        nDialog.setTitle(pTitle)

    if (pMsg.isNotEmpty())
        nDialog.setMessage(pMsg)

    if (pPositiveAction != null)
        nDialog.setPositiveButton(pPositiveText) { _, _ -> pPositiveAction() }

    if (pNegativeAction != null)
        nDialog.setNegativeButton(pNegativeText) { _, _ -> pNegativeAction() }

    val nCreatedDialog = nDialog.create()

    if (pInstantDialog)
        nCreatedDialog.show()

    return nCreatedDialog
}

fun Activity.exitDialog(
    pInstantDialog: Boolean = true
): AlertDialog {
    return confirmDialog(
        getString(R.string.exiting_app),
        getString(R.string.already_leave),
        pPositiveText = getString(R.string.yes),
        pNegativeText = getString(R.string.no),
        pPositiveAction = {
            this.vibrateOnClick()
            this.finish()
        },
        pNegativeAction = {
        },
        pInstantDialog = pInstantDialog
    )
}

fun <T> Context.customSearchListDialog(
    pTitle: Int,
    pItems: List<T>,
    pItemsAdapter: BaseOneItemListAdapter<T>,
    pFilter: (T, CharSequence) -> Boolean
): AlertDialog {
    val nDialogView = DialogChooseItemBinding.inflate(LayoutInflater.from(this))

    val nChooseItemDialog = AlertDialog.Builder(this)
        .setView(nDialogView.root)
        .setTitle(pTitle)
        .create()

    pItemsAdapter.setData(pItems)

    nDialogView.itemsSearch.doOnTextChanged { pText, _, _, _ ->
        if (!pText.isNullOrEmpty()) {
            pItemsAdapter.setData(pItems.filter { pFilter(it, pText) })
        } else {
            pItemsAdapter.setData(pItems)
        }
    }

    nDialogView.itemsList.layoutManager = LinearLayoutManager(this)
    nDialogView.itemsList.adapter = pItemsAdapter

    return nChooseItemDialog
}

fun <T> Context.showCustomSearchListDialog(
    pTitle: Int,
    pItems: List<T>,
    pItemsAdapter: BaseOneItemListAdapter<T>,
    pFilter: (T, CharSequence) -> Boolean
) {
    customSearchListDialog(pTitle, pItems, pItemsAdapter, pFilter).show()
}
