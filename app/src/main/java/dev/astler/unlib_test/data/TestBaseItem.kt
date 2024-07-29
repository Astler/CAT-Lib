package dev.astler.unlib_test.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.astler.ui.data.IFlexibleItem
import java.util.UUID

data class TestBaseItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    override var size: Int = 2,
    val hasNewContent: Boolean = false,
    override val uid: String = UUID.randomUUID().toString()
) : IFlexibleItem {
    override val iconId: Int
        get() = icon
    override val titleId: Int
        get() = title
}