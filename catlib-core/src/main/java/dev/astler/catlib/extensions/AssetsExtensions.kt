package dev.astler.catlib.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dev.astler.catlib.helpers.trackedTry
import java.nio.charset.Charset

fun Context.getBitmapFromAsset(path: String): Bitmap? {
    return trackedTry(fallbackValue = null) {
        BitmapFactory.decodeStream(assets.open(path))
    }
}

fun Context.readFileFromAssets(path: String): String {
    return trackedTry(fallbackValue = "Error $path") {
        val inputStream = assets.open(path)
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()

        String(byteArray, Charset.defaultCharset())
    }
}
