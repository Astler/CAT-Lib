package dev.astler.unli.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import java.io.IOException

fun Context.getBitmapFromAsset(strName: String): Bitmap? {
    return try {
        BitmapFactory.decodeStream(assets.open(strName))
    } catch (e: IOException) {
        log("UNLI: Image read failed: $e")
        null
    }
}

fun Context.createNoFilterDrawableFromBitmap(pBitmap: Bitmap, pColorRes: Int = -1): BitmapDrawable {
    val drawable = BitmapDrawable(resources, pBitmap)
    drawable.setAntiAlias(false)
    drawable.isFilterBitmap = false

    if (pColorRes != -1) {
        drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, pColorRes), PorterDuff.Mode.SRC_ATOP)
    }

    return drawable
}