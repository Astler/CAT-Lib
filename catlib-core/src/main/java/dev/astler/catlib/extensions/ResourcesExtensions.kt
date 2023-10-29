package dev.astler.catlib.extensions

import android.content.Context
import androidx.annotation.RawRes
import dev.astler.catlib.helpers.trackedTry
import java.nio.charset.Charset

fun Context.readFileFromRaw(@RawRes fileId: Int, errorFallback: String = ""): String {
    return trackedTry(fallbackValue = errorFallback) {
        val inputStream = resources.openRawResource(fileId)
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()

        String(byteArray, Charset.defaultCharset())
    }
}
