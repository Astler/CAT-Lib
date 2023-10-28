package dev.astler.cat_ui.utils.views

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
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import dev.astler.cat_ui.utils.tintDrawable
import dev.astler.cat_ui.utils.tintDrawableByAttr
import dev.astler.catlib.helpers.trackedTry
import okhttp3.HttpUrl
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
                val layers = arrayOf(drawable, *pAdditionalDrawables)
                setImageDrawable(LayerDrawable(layers))
            }

        imageLoader.enqueue(requestBuilder.build())
    }
}
