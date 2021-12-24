package dev.astler.cat_ui.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import dev.astler.cat_ui.R
import dev.astler.cat_ui.adapters.CatOneTypeAdapter
import dev.astler.cat_ui.databinding.DialogChooseItemBinding
import dev.astler.cat_ui.databinding.DialogChooseItemWithSearchBinding
import dev.astler.cat_ui.databinding.ItemPrefsTextBinding
import dev.astler.cat_ui.items.DialogSimpleTextItem
import dev.astler.unlib.utils.vibrateOnClick

fun Context.yesNoDialog(
    pTitle: String = "",
    pMsg: String = "",
    pPositiveAction: (() -> Unit)? = null,
    pInstantDialog: Boolean = true
): AlertDialog {
    val nDialog = AlertDialog.Builder(this)

    if (pTitle.isNotEmpty())
        nDialog.setTitle(pTitle)

    if (pMsg.isNotEmpty())
        nDialog.setMessage(pMsg)

    if (pPositiveAction != null)
        nDialog.setPositiveButton(R.string.yes) { _, _ -> pPositiveAction() }

    nDialog.setNegativeButton(R.string.no) { dialog, _ -> dialog?.dismiss() }

    val nCreatedDialog = nDialog.create()

    if (pInstantDialog)
        nCreatedDialog.show()

    return nCreatedDialog
}

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
    pOkAction: () -> Unit = {},
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
    pItemsAdapter: CatOneTypeAdapter<T>,
    pFilter: (T, CharSequence) -> Boolean
): AlertDialog {
    val nDialogView = DialogChooseItemWithSearchBinding.inflate(LayoutInflater.from(this))

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
    pItemsAdapter: CatOneTypeAdapter<T>,
    pFilter: (T, CharSequence) -> Boolean
) {
    customSearchListDialog(pTitle, pItems, pItemsAdapter, pFilter).show()
}

/**
 * NEW!
 */

fun <T> Context.listDialog(
    pTitle: Int,
    pItems: List<T>,
    pItemsAdapter: CatOneTypeAdapter<T>
): AlertDialog {
    val nDialogView = DialogChooseItemBinding.inflate(LayoutInflater.from(this))

    val nChooseItemDialog = AlertDialog.Builder(this)
        .setView(nDialogView.root)
        .setTitle(pTitle)
        .create()

    pItemsAdapter.setData(pItems)

    nDialogView.itemsList.layoutManager = LinearLayoutManager(this)
    nDialogView.itemsList.adapter = pItemsAdapter

    return nChooseItemDialog
}

fun Context.baseListDialog(
    pTitle: Int,
    pItems: List<DialogSimpleTextItem>,
    pOnItemClicked: (String) -> Unit
): AlertDialog {
    val nDialogView = DialogChooseItemBinding.inflate(LayoutInflater.from(this))

    val nChooseItemDialog = AlertDialog.Builder(this)
        .setView(nDialogView.root)
        .setTitle(pTitle)
        .create()

    val nItemsAdapter = CatOneTypeAdapter<DialogSimpleTextItem>(R.layout.item_prefs_text, { pData, pHolder ->
        val nMenuItemBind = ItemPrefsTextBinding.bind(pHolder.itemView)
        nMenuItemBind.text.text = pData.itemTitle

        nMenuItemBind.root.setOnClickListener {
            pOnItemClicked(pData.clickType)
            nChooseItemDialog.dismiss()
        }
    })

    nItemsAdapter.setData(pItems)

    nDialogView.itemsList.layoutManager = LinearLayoutManager(this)
    nDialogView.itemsList.adapter = nItemsAdapter

    return nChooseItemDialog
}
