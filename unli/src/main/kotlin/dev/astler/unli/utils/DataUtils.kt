package dev.astler.unli.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import dev.astler.unli.R
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
    return if(clipboard.primaryClip != null) {
        val item = clipboard.primaryClip?.getItemAt(0)
         item?.text?:""
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