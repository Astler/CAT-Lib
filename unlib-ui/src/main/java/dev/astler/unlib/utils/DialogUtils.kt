package dev.astler.unlib.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import dev.astler.unlib.adapters.BaseOneItemListAdapter
import dev.astler.unlib.core.R
import dev.astler.unlib.ui.databinding.DialogChooseItemBinding

fun Context.unLibInfoDialog(pText: String, pInstantShow: Boolean = true): AlertDialog {
    val nDialog = AlertDialog.Builder(this).setMessage(pText).create()

    if (pInstantShow)
        nDialog.show()

    return nDialog
}

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
    confirmDialog(
        R.string.exiting_app,
        getString(R.string.already_leave),
        getString(R.string.yes),
        getString(R.string.no),
        {
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
    unLibDialog(
        getString(pTitle),
        pMsg,
        pPositiveText,
        pNegativeText,
        pPositiveClick,
        pNegativeClick
    )
}

fun Context.confirmDialog(
    @StringRes pTitle: Int,
    pMsg: CharSequence,
    @StringRes pPositiveText: Int,
    @StringRes pNegativeText: Int,
    pPositiveClick: ((pDialogInterface: DialogInterface) -> Unit),
    pNegativeClick: ((pDialogInterface: DialogInterface) -> Unit)
) {
    unLibDialog(
        getString(pTitle),
        pMsg,
        getString(pPositiveText),
        getString(pNegativeText),
        pPositiveClick,
        pNegativeClick
    )
}

fun Context.unLibDialog(
    @StringRes pTitle: Int,
    pMsg: CharSequence,
    pPositiveText: CharSequence,
    pNegativeText: CharSequence,
    pPositiveClick: ((pDialogInterface: DialogInterface) -> Unit)? = null,
    pNegativeClick: ((pDialogInterface: DialogInterface) -> Unit)? = null
) {
    unLibDialog(
        getString(pTitle),
        pMsg,
        pPositiveText,
        pNegativeText,
        pPositiveClick,
        pNegativeClick
    )
}

fun Context.unLibDialog(
    @StringRes pTitle: Int,
    pMsg: CharSequence,
    @StringRes pPositiveText: Int,
    @StringRes pNegativeText: Int,
    pPositiveClick: ((pDialogInterface: DialogInterface) -> Unit)? = null,
    pNegativeClick: ((pDialogInterface: DialogInterface) -> Unit)? = null
) {
    unLibDialog(
        getString(pTitle),
        pMsg,
        getString(pPositiveText),
        getString(pNegativeText),
        pPositiveClick,
        pNegativeClick
    )
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

    pItemsAdapter.addItems(pItems)

    nDialogView.itemsSearch.doOnTextChanged { pText, _, _, _ ->
        if (!pText.isNullOrEmpty()) {
            pItemsAdapter.addItems(pItems.filter { pFilter(it, pText) })
        } else {
            pItemsAdapter.addItems(pItems)
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

/**
 * New dialogs era
 */

enum class RecyclerWithEditFieldDialogMode {
    EDIT, SEARCH, INFO
}

data class RecyclerWithEditFieldDialogProperties(
    val mTitleId: Int = -1,
    val mTitle: String = "Empty",
    val mMode: RecyclerWithEditFieldDialogMode = RecyclerWithEditFieldDialogMode.INFO,
    val mAction: (pInput: CharSequence) -> Unit = {}
)

fun <T> Context.recyclerWithFieldDialog(
    pProperties: RecyclerWithEditFieldDialogProperties,
    pItems: List<T>,
    pItemsAdapter: BaseOneItemListAdapter<T>,
    pFilter: (T, CharSequence) -> Boolean
): AlertDialog {
    val nDialogView = DialogChooseItemBinding.inflate(LayoutInflater.from(this))

    val nChooseItemDialogBuilder = AlertDialog.Builder(this)
        .setView(nDialogView.root)

    if (pProperties.mTitleId == -1) nChooseItemDialogBuilder.setTitle(pProperties.mTitle)
    else nChooseItemDialogBuilder.setTitle(pProperties.mTitleId)

    when (pProperties.mMode) {
        RecyclerWithEditFieldDialogMode.SEARCH -> {
            nDialogView.searchBar.showView()
            nDialogView.actionButton.goneView()
            nDialogView.itemsSearch.doOnTextChanged { pText, _, _, _ ->
                if (!pText.isNullOrEmpty()) {
                    pItemsAdapter.addItems(pItems.filter { pFilter(it, pText) })
                } else {
                    pItemsAdapter.addItems(pItems)
                }
            }
        }
        RecyclerWithEditFieldDialogMode.EDIT -> {
            nDialogView.searchBar.showView()
            nDialogView.actionButton.showView()
            nDialogView.actionButton.setOnClickListener {
                pProperties.mAction(nDialogView.itemsSearch.text.toString())
            }
        }
        else -> {
            nDialogView.searchBar.goneView()
        }
    }

    val nChooseItemDialog = nChooseItemDialogBuilder.create()

    pItemsAdapter.addItems(pItems)

    nDialogView.itemsList.layoutManager = LinearLayoutManager(this)
    nDialogView.itemsList.adapter = pItemsAdapter

    return nChooseItemDialog
}