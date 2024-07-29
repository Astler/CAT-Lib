package dev.astler.ui.utils.dialogs

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.astler.ui.adapters.CatOneTypeAdapter
import dev.astler.ui.items.DialogSimpleTextItem
import dev.astler.ui.utils.tryToGetTextFrom
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.ui.R
import dev.astler.ui.databinding.DialogChooseItemBinding
import dev.astler.ui.databinding.DialogChooseItemWithSearchBinding
import dev.astler.ui.databinding.DialogEditTextBinding
import dev.astler.ui.databinding.ItemPrefsTextBinding

data class CatDialogData(
    val title: Any? = null,
    val message: Any? = null,
    val positive: Any? = null,
    val negative: Any? = null,
    val positiveAction: ((DialogInterface) -> Unit)? = null,
    val negativeAction: ((DialogInterface) -> Unit)? = null,
    val showAfterCreation: Boolean = true
)

fun Activity.privacyPolicyDialog(appConfig: AppConfig, preferences: PreferencesTool) {
    val builder = MaterialAlertDialogBuilder(this)
    builder.setTitle(gg.pressf.resources.R.string.privacy_policy)
    builder.setCancelable(false)
    builder.setMessage(gg.pressf.resources.R.string.privacy_policy_request)

    builder.setPositiveButton(gg.pressf.resources.R.string.read_privacy_policy) { _, _ ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appConfig.policyLink))
        startActivity(intent)
        privacyPolicyDialog(appConfig, preferences)
    }

    builder.setNegativeButton(gg.pressf.resources.R.string.exit) { dialog, which ->
        finish()
    }

    builder.setNeutralButton(gg.pressf.resources.R.string.yes_agree) { dialog, which ->
        preferences.isPolicyAnswered = true
        dialog.dismiss()
    }

    builder.create().show()
}


fun Context.editTextDialog(
    showAfterCreation: Boolean = true,
    title: Any? = null,
    message: Any? = null,
    initText: String = "",
    positive: Any? = gg.pressf.resources.R.string.ok,
    okClicked: (DialogInterface, String) -> Unit = { _, _ -> }
): AlertDialog {
    val dialogView = DialogEditTextBinding.inflate(LayoutInflater.from(this))

    val titleText = tryToGetTextFrom(title)
    val messageText = tryToGetTextFrom(message)
    val positiveText = tryToGetTextFrom(positive)

    dialogView.editField.setText(initText)
    dialogView.description.text = messageText

    val dialog = MaterialAlertDialogBuilder(this)
        .setTitle(titleText)
        .setView(dialogView.root)
        .setPositiveButton(positiveText) { dialog, _ ->
            okClicked.invoke(dialog, dialogView.editField.text.toString())
        }
        .setNegativeButton(android.R.string.cancel) { _, _ -> }.create()

    if (showAfterCreation)
        dialog.show()

    return dialog
}


fun Context.yesNoDialog(
    title: Any? = null,
    message: Any? = null,
    showAfterCreation: Boolean = true,
    negativeAction: ((DialogInterface) -> Unit)? = {},
    positiveAction: ((DialogInterface) -> Unit)? = {}
): AlertDialog {
    return catDialog(
        CatDialogData(
            title = title,
            message = message,
            negative = gg.pressf.resources.R.string.no,
            negativeAction = negativeAction,
            positive = gg.pressf.resources.R.string.yes,
            positiveAction = positiveAction,
            showAfterCreation = showAfterCreation
        )
    )
}

fun Context.okDialog(
    title: Any? = null,
    message: Any? = null,
    showAfterCreation: Boolean = true,
    okAction: ((DialogInterface) -> Unit) = {}
): AlertDialog {
    return catDialog(
        CatDialogData(
            title = title,
            message = message,
            positive = gg.pressf.resources.R.string.ok,
            positiveAction = okAction,
            showAfterCreation = showAfterCreation
        )
    )
}

fun Context.unpackedCatDialog(
    title: Any? = null,
    message: Any? = null,
    negative: Any? = null,
    positive: Any? = null,
    showAfterCreation: Boolean = true,
    negativeAction: ((DialogInterface) -> Unit)? = null,
    positiveAction: ((DialogInterface) -> Unit)? = null
): AlertDialog {
    return catDialog(
        CatDialogData(
            title = title,
            message = message,
            negative = negative,
            positive = positive,
            negativeAction = negativeAction,
            positiveAction = positiveAction,
            showAfterCreation = showAfterCreation
        )
    )
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
    title: Any,
    items: List<T>,
    cotAdapter: CatOneTypeAdapter<T>,
    negative: Any? = null,
    positive: Any? = null,
    negativeAction: ((DialogInterface) -> Unit)? = null,
    positiveAction: ((DialogInterface) -> Unit)? = null
): AlertDialog {
    val dialogView = DialogChooseItemBinding.inflate(LayoutInflater.from(this))

    val titleText = tryToGetTextFrom(title)
    val positiveText = tryToGetTextFrom(positive)
    val negativeText = tryToGetTextFrom(negative)

    val materialDialog =
        MaterialAlertDialogBuilder(this).setView(dialogView.root).setTitle(titleText)

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

    cotAdapter.setData(items)

    dialogView.itemsList.layoutManager = LinearLayoutManager(this)
    dialogView.itemsList.adapter = cotAdapter

    val createdDialog = materialDialog.create()

    if (showAfterCreation) createdDialog.show()

    return createdDialog
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

private fun Context.catDialog(data: CatDialogData): AlertDialog {
    val materialDialog = MaterialAlertDialogBuilder(this)

    val titleText = tryToGetTextFrom(data.title)
    val messageText = tryToGetTextFrom(data.message)
    val positiveText = tryToGetTextFrom(data.positive)
    val negativeText = tryToGetTextFrom(data.negative)

    if (titleText != null) {
        materialDialog.setTitle(titleText)
    }

    if (messageText != null) {
        materialDialog.setMessage(messageText)
    }

    if (data.positiveAction != null && positiveText != null) materialDialog.setPositiveButton(
        positiveText
    ) { dialog, _ ->
        data.positiveAction.invoke(dialog)
    }

    if (data.negativeAction != null && negativeText != null) materialDialog.setNegativeButton(
        negativeText
    ) { dialog, _ ->
        data.negativeAction.invoke(dialog)
    }

    val dialog = materialDialog.create()

    if (data.showAfterCreation)
        dialog.show()

    return dialog
}
