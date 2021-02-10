package dev.astler.unlib.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import dev.astler.unlib.R
import java.util.*

fun Context.getStringResourceId(string: String): Int {
    val id = resources.getIdentifier(string, "string", packageName)
    return if (id != 0) id else R.string.nothing
}

fun Context.getStringResource(
    string: String,
    returnDef: String = string,
    locale: Locale? = null
): String {
    val stringId =
        resources.getIdentifier(string, "string", packageName)
    return if (stringId != 0) {
        if (locale != null)
            getStringResByLanguage(stringId, locale)
        else
            getString(stringId)
    } else returnDef
}

fun Context.getStringResByLanguage(id: Int, locale: Locale): String {
    return if (id != 0) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val configuration = Configuration()
            configuration.setLocale(locale)
            createConfigurationContext(configuration).getText(id).toString()
        } else {
            getString(id)
        }
    } else "SWW"
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

fun Context.getDrawableByName(pDrawableName: String): Drawable? {
    val nDrawableId = resources.getIdentifier(pDrawableName, "drawable", packageName)

    return if (nDrawableId != 0) ContextCompat.getDrawable(
            this,
            nDrawableId
    ) else ContextCompat.getDrawable(this, R.drawable.file)
}