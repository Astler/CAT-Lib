package dev.astler.unli.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import dev.astler.unli.R

fun Context.getStringResourceId(string: String): Int {
    val id = resources.getIdentifier(string, "string", packageName)
    return if (id != 0) id else R.string.nothing
}

fun Context.getStringResource(string: String, returnDef: String = string): String {
    val stringId =
        resources.getIdentifier(string, "string", packageName)
    return if (stringId != 0) getString(stringId) else returnDef
}

fun Context.tintDrawable(
    @DrawableRes icon: Int,
    @ColorRes colorId: Int
): Drawable? {
    val drawable = ContextCompat.getDrawable(this, icon)

    drawable?.let {
        val color = ContextCompat.getColor(this, colorId)
        DrawableCompat.setTint(it, color)
    }

    return drawable
}

fun Context.tintDrawableByAttr(
    @DrawableRes icon: Int,
    @AttrRes attrId: Int
): Drawable? {
    val drawable = ContextCompat.getDrawable(this, icon)

    drawable?.let {
        val color = getColorFromAttr(attrId)
        DrawableCompat.setTint(it, color)
    }

    return drawable
}

fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}