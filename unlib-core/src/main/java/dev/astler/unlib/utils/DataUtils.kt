package dev.astler.unlib.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import dev.astler.unlib.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*


fun Date.toHumanView(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)
}

fun String.humanViewToMillis(): Long {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val date = simpleDateFormat.parse(this)

    return date?.time ?: 0L
}


fun Int.timeToString(): String? {
    val seconds = this % 60
    val minutes = (this - seconds) / 60 % 60
    val hours = (this - minutes - seconds) / 3600
    val h = if (hours < 10) "0$hours" else hours.toString()
    val m = if (minutes < 10) "0$minutes" else minutes.toString()
    val s = if (seconds < 10) "0$seconds" else seconds.toString()

    return "$h:$m:$s"
}

fun Long.millisToHumanView(): String {
    return Date(this).toHumanView()
}

fun Bitmap.createLocalImage(context: Context, name: String) {
    try {
        val file = File(context.filesDir, name)
        val out = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.JPEG, 60, out)
        out.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun Context.copyToBuffer(pData: CharSequence, pToast: Toast?): Toast? {
    var nToast = pToast

    if (pData.isNotEmpty()) {
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", pData)
        myClipboard.setPrimaryClip(myClip)

        nToast?.cancel()
        nToast = Toast.makeText(this, getString(R.string.copied_to_buffer, pData), Toast.LENGTH_SHORT)
        nToast?.show()
    }

    return nToast
}

fun Context.copyToBuffer(text: CharSequence) {
    if (text.isNotEmpty()) {
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", text)
        myClipboard.setPrimaryClip(myClip)
        Toast.makeText(this, R.string.copy_in_buffer, Toast.LENGTH_SHORT).show()
    }
}

fun Context.readFromBuffer(): CharSequence {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return if (clipboard.primaryClip != null) {
        val item = clipboard.primaryClip?.getItemAt(0)
        item?.text ?: ""
    } else {
        ""
    }
}

fun Context.readFileFromAssets(path: String): String {
    return try {
        val inputStream = assets.open(path)
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()

        String(byteArray, Charset.defaultCharset())

    } catch (e: Exception) {
        "SWW"
    }
}

fun Context.readFileFromFiles(pFileWithPath: String): String {
    return try {
        val inputStream = File(filesDir, pFileWithPath).inputStream()
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()

        String(byteArray, Charset.defaultCharset())

    } catch (e: Exception) {
        "SWW"
    }
}