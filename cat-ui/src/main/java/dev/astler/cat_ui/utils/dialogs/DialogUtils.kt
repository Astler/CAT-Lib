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
import dev.astler.unlib.utils.vibrateOnClick

fun Context.yesNoDialog(
    title: Any? = null, message: Any? = null, positiveAction: ((DialogInterface) -> Unit)? = null
): AlertDialog {
    return catDialog(
        title, message, R.string.no, { it.dismiss() }, R.string.yes
    ) { positiveAction?.invoke(it) }
}

fun Context.okDialog(
    title: Any? = null, message: Any? = null, okAction: ((DialogInterface) -> Unit)? = null
): AlertDialog {
    return catDialog(
        title, message, positive = R.string.ok, positiveAction = okAction
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
    return catDialog(
        title,
        message,
        negative = negative,
        negativeAction = negativeAction,
        positive = positive,
        positiveAction = positiveAction
    )
}

fun Activity.exitDialog(): AlertDialog {
    return catDialog(R.string.exiting_app,
        R.string.already_leave,
        R.string.no,
        { it.dismiss() },
        R.string.yes,
        {
            this.vibrateOnClick()
            this.finish()
        })
}

fun <T> Context.searchListDialog(
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

    return customChooseDialog
}

fun <T> Context.listDialog(
    pTitle: Int, pItems: List<T>, pItemsAdapter: CatOneTypeAdapter<T>
): AlertDialog {
    val dialogView = DialogChooseItemBinding.inflate(LayoutInflater.from(this))

    val listDialog = MaterialAlertDialogBuilder(this).setView(dialogView.root).setTitle(pTitle).create()

    pItemsAdapter.setData(pItems)

    dialogView.itemsList.layoutManager = LinearLayoutManager(this)
    dialogView.itemsList.adapter = pItemsAdapter

    return listDialog
}

fun Context.simpleListDialog(
    title: Int, items: List<DialogSimpleTextItem>, itemClicked: (String) -> Unit
): AlertDialog {
    val nDialogView = DialogChooseItemBinding.inflate(LayoutInflater.from(this))

    val nChooseItemDialog =
        MaterialAlertDialogBuilder(this).setView(nDialogView.root).setTitle(title).create()

    val nItemsAdapter =
        CatOneTypeAdapter<DialogSimpleTextItem>(R.layout.item_prefs_text, { pData, pHolder ->
            val nMenuItemBind = ItemPrefsTextBinding.bind(pHolder.itemView)
            nMenuItemBind.text.text = pData.itemTitle

            nMenuItemBind.root.setOnClickListener {
                itemClicked(pData.clickType)
                nChooseItemDialog.dismiss()
            }
        })

    nItemsAdapter.setData(items)

    nDialogView.itemsList.layoutManager = LinearLayoutManager(this)
    nDialogView.itemsList.adapter = nItemsAdapter

    return nChooseItemDialog
}

private fun Context.catDialog(
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

    return materialDialog.create()
}
