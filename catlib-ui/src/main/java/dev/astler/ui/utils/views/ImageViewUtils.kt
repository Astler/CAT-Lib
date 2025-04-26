package dev.astler.ui.utils.views

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import coil3.ImageLoader
import coil3.request.Disposable
import coil3.request.ImageRequest
import dev.astler.ui.utils.tintDrawable
import dev.astler.ui.utils.tintDrawableByAttr
import dev.astler.catlib.helpers.trackedTry
import java.io.File

fun ImageView.setColorTintDrawable(@DrawableRes icon: Int, @ColorRes colorId: Int) {
    setImageDrawable(context.tintDrawable(icon, colorId))
}

fun ImageView.setAttrTintDrawable(@DrawableRes icon: Int, @AttrRes attrId: Int) {
    setImageDrawable(context.tintDrawableByAttr(icon, attrId))
}

fun ImageView.loadWithBackground(pRequest: String, pBackgroundColor: Int): Disposable? {
    return trackedTry(fallbackValue = null) {
        val imageLoader = ImageLoader(context)
        val requestBuilder = ImageRequest.Builder(context)
            .data(pRequest)
            .target { drawable ->
                if (drawable !is Drawable) return@target
                val background = ShapeDrawable()
                background.paint.color =
                    ContextCompat.getColor(context, pBackgroundColor)
                val layers = arrayOf(background, drawable)
                setImageDrawable(LayerDrawable(layers))
            }

        imageLoader.enqueue(requestBuilder.build())
    }
}

/**
 * Set the data to load.
 *
 * The default supported data types are:
 * - [String] (mapped to a [Uri])
 * - [Uri] ("android.resource", "content", "file", "http", and "https" schemes only)
 * - [HttpUrl]
 * - [File]
 * - [DrawableRes]
 * - [Drawable]
 * - [Bitmap]
 */
fun ImageView.mixDrawables(pRequest: Any?, vararg pAdditionalDrawables: Drawable): Disposable? {
    return trackedTry(fallbackValue = null) {
        val imageLoader = ImageLoader(context)

        val requestBuilder = ImageRequest.Builder(context)
            .data(pRequest)
            .target { drawable ->
                if (drawable !is Drawable) return@target
                val layers = arrayOfNulls<Drawable>(1 + pAdditionalDrawables.size)
                layers[0] = drawable
                for (i in pAdditionalDrawables.indices) {
                    layers[i + 1] = pAdditionalDrawables[i]
                }
                setImageDrawable(LayerDrawable(layers))
            }

        imageLoader.enqueue(requestBuilder.build())
    }
}
