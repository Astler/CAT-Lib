package dev.astler.unlib.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
        drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, pColorRes), PorterDuff.Mode.SRC_ATOP)
    }

    return drawable
}

fun ImageView.safeGlideLoadWithBackground(
        pRequest: String,
        pBackgroundColor: Int) {
    try {
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(64, 64)

        Glide.with(this)
                .load(pRequest)
                .thumbnail(0.25f)
                .apply(requestOptions)
                .into(object : CustomTarget<Drawable>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                    ) {
                        val background = ShapeDrawable()
                        background.paint.color =
                                ContextCompat.getColor(context, pBackgroundColor)

                        val layers = arrayOf(background, resource)

                        setImageDrawable(LayerDrawable(layers))
                    }
                })

    } catch (e: Exception) {
        infoLog("UNLI: Image glide load failed: $e")
    }
}

fun ImageView.safeGlideLoad(pRequest: String) {
    try {
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(64, 64)

        Glide.with(this)
            .load(pRequest).thumbnail(0.25f)
            .apply(requestOptions).into(this)

    } catch (e: Exception) {
        infoLog("UNLI: Image glide load failed: $e")
    }
}