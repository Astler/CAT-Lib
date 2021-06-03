package dev.astler.unlib.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import coil.ImageLoader
import coil.request.ImageRequest
import java.io.IOException

fun Context.getBitmapFromAsset(strName: String): Bitmap? {
    return try {
        BitmapFactory.decodeStream(assets.open(strName))
    } catch (e: IOException) {
        infoLog("UNLI: Image read failed: $e")
        null
    }
}

fun Context.createNoFilterDrawableFromBitmap(pBitmap: Bitmap, pColorRes: Int = -1): BitmapDrawable {
    val drawable = BitmapDrawable(resources, pBitmap)
    drawable.setAntiAlias(false)
    drawable.isFilterBitmap = false

    if (pColorRes != -1) {
        val color = ContextCompat.getColor(this, pColorRes)
        DrawableCompat.setTint(drawable, color)
    }

    return drawable
}

fun ImageView.loadWithBackground(
    pRequest: String,
    pBackgroundColor: Int
) {
    simpleTry(
        context
    ) {
        val imageLoader = ImageLoader(context)

        val request = ImageRequest.Builder(context)
            .data(pRequest)
            .target { drawable ->
                val background = ShapeDrawable()
                background.paint.color =
                    ContextCompat.getColor(context, pBackgroundColor)

                val layers = arrayOf(background, drawable)

                setImageDrawable(LayerDrawable(layers))
            }
            .build()

        val disposable = imageLoader.enqueue(request)
    }
}
