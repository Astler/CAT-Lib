package dev.astler.ui.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface IFlexibleItem {
    @get:DrawableRes
    val iconId: Int?

    @get:StringRes
    val titleId: Int?

    val uid: String
    var size: Int
}