package dev.astler.cat_ui.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import dev.astler.unlib.core.R
import java.util.* // ktlint-disable no-wildcard-imports

fun dpToPixels(dips: Float): Float = dips * Resources.getSystem().displayMetrics.density + 0.5f

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

fun Drawable.toBitmap(): Bitmap? {
    if (this is BitmapDrawable) {
        if (this.bitmap != null) {
            return this.bitmap
        }
    }

    val bitmap: Bitmap? = if (this.intrinsicWidth <= 0 || this.intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        )
    } else {
        Bitmap.createBitmap(
            this.intrinsicWidth * 13,
            this.intrinsicHeight * 13,
            Bitmap.Config.ARGB_8888
        )
    }

    if (bitmap != null) {
        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)
    }

    return bitmap
}

fun Context.getContextColor(@ColorRes pColorId: Int): Int {
    return ContextCompat.getColor(this, pColorId)
}
