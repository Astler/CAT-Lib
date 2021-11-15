package dev.astler.unlib.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import okhttp3.HttpUrl
import java.io.File

fun ImageView.loadWithBackground(pRequest: String, pBackgroundColor: Int): Disposable? {
    return tryWithDefault(null) {
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
    return tryWithDefault(null) {
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
