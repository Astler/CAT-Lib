package dev.astler.unlib.utils.views

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.chip.Chip

fun Context.simpleTextChip(@StringRes pTextId: Int): View {
    return simpleTextChip(getString(pTextId))
}

fun Context.simpleTextChip(pText: String): View {
    val mChip = Chip(this)
    mChip.text = pText

    return mChip
}
