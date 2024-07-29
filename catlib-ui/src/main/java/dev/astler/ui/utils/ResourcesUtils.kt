package dev.astler.ui.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import dev.astler.ui.R
import java.util.*

fun Context.tryToGetTextFrom(value: Any?): String? {
    if (value != null) {
        val isNumber = value is Number

        if (isNumber) {
            return getString(value as Int)
        } else {
            val stringValue = value.toString()

            if (stringValue.isNotEmpty()) {
                return stringValue
            }
        }
    }

    return null
}

fun dpToPixels(dips: Float): Float = dips * Resources.getSystem().displayMetrics.density + 0.5f

fun Context.getStringResourceId(string: String): Int {
    val id = resources.getIdentifier(string, "string", packageName)
    return if (id != 0) id else gg.pressf.resources.R.string.nothing
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
        val configuration = Configuration()
        configuration.setLocale(locale)
        createConfigurationContext(configuration).getText(id).toString()
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

fun Bitmap.toNoFilterDrawable(context: Context, pColorRes: Int = -1): BitmapDrawable {
    val drawable = BitmapDrawable(context.resources, this)
    drawable.setAntiAlias(false)
    drawable.isFilterBitmap = false

    if (pColorRes != -1) {
        val color = ContextCompat.getColor(context, pColorRes)
        DrawableCompat.setTint(drawable, color)
    }

    return drawable
}

fun Context.tintDrawableByAttr(
    @DrawableRes icon: Int,
    @AttrRes attrId: Int
): Drawable? {
    val drawable = ContextCompat.getDrawable(this, icon)

    drawable?.let {
        val color = getAttributeColor(attrId)
        DrawableCompat.setTint(it, color)
    }

    return drawable
}

fun Context.getAttributeColor(
    @AttrRes attrColor: Int,
    alpha: Float = -1f,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)

    if (alpha == -1f) {
        return typedValue.data
    }

    return typedValue.data.setAlpha(alpha)
}

fun Context.getDimensionFromAttr(resId: Int): Int {
    val value = TypedValue()

    if (!theme.resolveAttribute(resId, value, true)) return 0
    return if (value.type == TypedValue.TYPE_DIMENSION) {
        val indexOfAttrTextSize = 0
        val a: TypedArray = obtainStyledAttributes(value.data, intArrayOf(resId))
        val textSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1)
        a.recycle()
        textSize
    } else {
        0
    }
}

fun Context.getDrawableByName(pDrawableName: String): Drawable? {
    val nDrawableId = resources.getIdentifier(pDrawableName, "drawable", packageName)

    return if (nDrawableId != 0) ContextCompat.getDrawable(
        this,
        nDrawableId
    ) else ContextCompat.getDrawable(this, R.drawable.file)
}

fun Context.getContextColor(@ColorRes pColorId: Int): Int {
    return ContextCompat.getColor(this, pColorId)
}
