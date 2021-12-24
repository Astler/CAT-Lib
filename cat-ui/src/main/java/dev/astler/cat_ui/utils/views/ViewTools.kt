package dev.astler.cat_ui.utils.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import dev.astler.cat_ui.utils.getContextColor
import dev.astler.unlib.utils.infoLog

fun Context.inflateById(@LayoutRes pId: Int, pParent: ViewGroup? = null, pAttachToRoot: Boolean = false): View {
    return LayoutInflater.from(this).inflate(pId, pParent, pAttachToRoot)
}

fun View.inflateById(@LayoutRes pId: Int, pParent: ViewGroup? = null, pAttachToRoot: Boolean = false): View {
    return this.context.inflateById(pId, pParent, pAttachToRoot)
}

fun View.getContextColor(@ColorRes pColorId: Int): Int {
    return context.getContextColor(pColorId)
}

fun View.getCaptureBitmap(): Bitmap? {
    var screenshot: Bitmap? = null
    try {
        screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        draw(canvas)
    } catch (e: Exception) {
        infoLog("UNLIB: Failed to capture screenshot because:" + e.message)
    }

    return screenshot
}
