package dev.astler.cat_ui.utils.dialogs

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.astler.cat_ui.R
import dev.astler.cat_ui.adapters.CatOneTypeAdapter
import dev.astler.cat_ui.databinding.DialogChooseItemBinding
import dev.astler.cat_ui.databinding.DialogChooseItemWithSearchBinding
import dev.astler.cat_ui.databinding.ItemPrefsTextBinding
import dev.astler.cat_ui.items.DialogSimpleTextItem
import dev.astler.cat_ui.utils.tryToGetTextFrom
import dev.astler.catlib.utils.vibrateOnClick

/**
 * Predefined yes/no options dialog
 */

fun Context.yesNoDialog(
    showAfterCreation: Boolean = true,
    title: Any? = null,
    message: Any? = null,
    positiveAction: ((DialogInterface) -> Unit)? = {}
): AlertDialog {
    return catDialog(
        showAfterCreation,
        title = title,
        message = message,
        negative = R.string.no,
        negativeAction = { it.dismiss() },
        positive = R.string.yes
    ) { positiveAction?.invoke(it) }
}

fun Context.yesNoDialog(
    title: Any? = null,
    message: Any? = null,
    positiveAction: ((DialogInterface) -> Unit)? = { }
): AlertDialog {
    return yesNoDialog(
        showAfterCreation = true,
        title = title,
        message = message,
    ) { positiveAction?.invoke(it) }
}


/**
 * Predefined ok dialog
 */

fun Context.okDialog(
    showAfterCreation: Boolean = true,
    title: Any? = null,
    message: Any? = null,
    okAction: ((DialogInterface) -> Unit) = {}
): AlertDialog {
    return catDialog(
        showAfterCreation,
        title = title,
        message = message,
        positive = R.string.ok,
        positiveAction = okAction
    )
}

fun Context.okDialog(
    title: Any? = null,
    message: Any? = null,
    okAction: ((DialogInterface) -> Unit) = {}
): AlertDialog {
    return okDialog(
        showAfterCreation = true,
        title = title,
        message = message,
        okAction = okAction
    )
}

/**
 * Predefined confirm dialog. Just public variant for catDialog
 */

fun Context.confirmDialog(
    showAfterCreation: Boolean = true,
    title: Any? = null,
    message: Any? = null,
    negative: Any? = null,
    positive: Any? = null,
    negativeAction: ((DialogInterface) -> Unit)? = null,
    positiveAction: ((DialogInterface) -> Unit)? = null
): AlertDialog {
    return catDialog(
        showAfterCreation,
        title,
        message,
        negative = negative,
        negativeAction = negativeAction,
        positive = positive,
        positiveAction = positiveAction
    )
}

fun Context.confirmDialog(
    title: Any? = null,
    message: Any? = null,
    negative: Any? = null,
    positive: Any? = null,
    negativeAction: ((DialogInterface) -> Unit)? = null,
    positiveAction: ((DialogInterface) -> Unit)? = null
): AlertDialog {
    return confirmDialog(
        showAfterCreation = true,
        title = title,
        message = message,
        negative = negative,
        negativeAction = negativeAction,
        positive = positive,
        positiveAction = positiveAction
    )
}

fun Activity.exitDialog(): AlertDialog {
    return catDialog(title = R.string.exiting_app,
        message = R.string.already_leave,
        negative = R.string.no,
        negativeAction = { it.dismiss() },
        positive = R.string.yes,
        positiveAction = {
            this.vibrateOnClick()
            this.finish()
        })
}

fun <T> Context.searchListDialog(
    showAfterCreation: Boolean = true,
    title: Any?,
    listItems: List<T>,
    adapter: CatOneTypeAdapter<T>,
    filterLogic: ((T, CharSequence) -> Boolean)? = null
): AlertDialog {
    val titleText = tryToGetTextFrom(title)

    val dialogView = DialogChooseItemWithSearchBinding.inflate(LayoutInflater.from(this))

    val customChooseDialog =
        MaterialAlertDialogBuilder(this).setView(dialogView.root).setTitle(titleText).create()

    adapter.setData(listItems)

    if (filterLogic != null) dialogView.itemsSearch.doOnTextChanged { filterText, _, _, _ ->
        if (!filterText.isNullOrEmpty()) {
            adapter.setData(listItems.filter { filterLogic(it, filterText) })
        } else {
            adapter.setData(listItems)
        }
    }

    dialogView.itemsList.layoutManager = LinearLayoutManager(this)
    dialogView.itemsList.adapter = adapter

    if (showAfterCreation) customChooseDialog.show()

    return customChooseDialog
}

fun <T> Context.listDialog(
    showAfterCreation: Boolean = true,
    pTitle: Int,
    pItems: List<T>,
    pItemsAdapter: CatOneTypeAdapter<T>
): AlertDialog {
    val dialogView = DialogChooseItemBinding.inflate(LayoutInflater.from(this))

    val listDialog =
        MaterialAlertDialogBuilder(this).setView(dialogView.root).setTitle(pTitle).create()

    pItemsAdapter.setData(pItems)

    dialogView.itemsList.layoutManager = LinearLayoutManager(this)
    dialogView.itemsList.adapter = pItemsAdapter

    if (showAfterCreation) listDialog.show()

    return listDialog
}

fun Context.simpleListDialog(
    showAfterCreation: Boolean = true,
    title: Int,
    items: List<DialogSimpleTextItem>,
    itemClicked: (String) -> Unit
): AlertDialog {
    val dialogView = DialogChooseItemBinding.inflate(LayoutInflater.from(this))

    val nChooseItemDialog =
        MaterialAlertDialogBuilder(this).setView(dialogView.root).setTitle(title).create()

    val itemsAdapter =
        CatOneTypeAdapter<DialogSimpleTextItem>(R.layout.item_prefs_text, { pData, pHolder ->
            val nMenuItemBind = ItemPrefsTextBinding.bind(pHolder.itemView)
            nMenuItemBind.text.text = pData.itemTitle

            nMenuItemBind.root.setOnClickListener {
                itemClicked(pData.clickType)
                nChooseItemDialog.dismiss()
            }
        })

    itemsAdapter.setData(items)

    dialogView.itemsList.layoutManager = LinearLayoutManager(this)
    dialogView.itemsList.adapter = itemsAdapter

    if (showAfterCreation) nChooseItemDialog.show()

    return nChooseItemDialog
}

private fun Context.catDialog(
    showAfterCreation: Boolean = true,
    title: Any? = null,
    message: Any? = null,
    negative: Any? = null,
    negativeAction: ((DialogInterface) -> Unit)? = null,
    positive: Any? = null,
    positiveAction: ((DialogInterface) -> Unit)? = null
): AlertDialog {
    val materialDialog = MaterialAlertDialogBuilder(this)

    val titleText = tryToGetTextFrom(title)
    val messageText = tryToGetTextFrom(message)
    val positiveText = tryToGetTextFrom(positive)
    val negativeText = tryToGetTextFrom(negative)

    if (titleText != null) {
        materialDialog.setTitle(titleText)
    }

    if (messageText != null) {
        materialDialog.setMessage(messageText)
    }

    if (positiveAction != null && positiveText != null) materialDialog.setPositiveButton(
        positiveText
    ) { dialog, _ ->
        positiveAction(dialog)
    }

    if (negativeAction != null && negativeText != null) materialDialog.setNegativeButton(
        negativeText
    ) { dialog, _ ->
        negativeAction(dialog)
    }

    val dialog = materialDialog.create()

    if (showAfterCreation)
        dialog.show()

    return dialog
}
