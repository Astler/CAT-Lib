package dev.astler.cat_ui.utils

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import dev.astler.catlib.utils.errorLog

fun Context.generateImageShortcut(
    @DrawableRes imageId: Int,
    @AttrRes tintAttr: Int = -1,
    tintColor: Int = -1
): String {
    val name: String = resources.getResourceEntryName(imageId)

    if (tintColor == -1 && tintAttr == -1) {
        return "[img src=$name/]"
    }

    var colorHex = "";

    if (tintColor != -1) {
        colorHex = tintColor.toHexColor()
    }

    if (tintAttr != -1) {
        colorHex = getAttributeColor(tintAttr).toHexColor()
    }

    if (colorHex.isEmpty()) {
        errorLog("Invalid color: $colorHex for resource $name")
    }

    return "[img src=$name tint=$colorHex/]"
}