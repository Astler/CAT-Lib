package dev.astler.unlib.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import dev.astler.unlib.core.R
import dev.astler.unlib.utils.IntentsUtils.Companion.choosePictureActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class IntentsUtils {
    companion object {
        const val choosePictureActivity = 12
    }
}

@Suppress("DEPRECATION")
fun Context.rateIntentForUri(url: String, pPackageName: String = packageName): Intent {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(String.format("%s?id=%s", url, pPackageName))
    )

    var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK

    flags = if (Build.VERSION.SDK_INT >= 21) {
        flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
    } else {
        flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
    }

    intent.addFlags(flags)

    return intent
}

fun Fragment.pickImageFromIntent() {
    val getIntent = Intent(Intent.ACTION_GET_CONTENT)
    getIntent.type = "image/*"

    val pickIntent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    pickIntent.type = "image/*"

    val chooserIntent = Intent.createChooser(getIntent, "Select Image")
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

    startActivityForResult(chooserIntent, choosePictureActivity)
}

fun shareTextIntent(text: CharSequence): Intent {
    val intentShare = Intent(Intent.ACTION_SEND)
    intentShare.type = "text/plain"
    intentShare.putExtra(Intent.EXTRA_TEXT, text)
    return intentShare
}

fun Context.shareText(pText: CharSequence) {
    try {
        this.startActivity(shareTextIntent(pText))
    }
    catch (e: Exception) {}
}

fun Context.openWebUrl(pUrl: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pUrl)))
    } catch (e: Exception) {
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT)
                .show()
    }
}

fun Context.shareImagesDirBitmap(pPath: String = "shareImage.png"){
    val wrapper = ContextWrapper(this)
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)
    file = File(file, pPath)

    try {
        val contentUri = FileProvider.getUriForFile(this, this.packageName + ".provider", file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.type = "image/*"
        this.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}