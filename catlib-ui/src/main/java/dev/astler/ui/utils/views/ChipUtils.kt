package dev.astler.ui.utils.views

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

fun ChipGroup.addTextChip(@StringRes textId: Int) {
    addView(context.simpleTextChip(textId))
}

fun ChipGroup.addTextChip(text: String) {
    addView(context.simpleTextChip(text))
}

fun Context.simpleTextChip(@StringRes pTextId: Int): View {
    return simpleTextChip(getString(pTextId))
}

fun Context.simpleTextChip(pText: String): View {
    val mChip = Chip(this)
    mChip.text = pText

    return mChip
}
