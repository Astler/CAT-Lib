package dev.astler.unlib.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import dev.astler.unlib.cMimetypeImages
import dev.astler.unlib.cRegistryKey
import dev.astler.unlib.core.R
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class ImagePicker(
    pActivityResultRegistry: ActivityResultRegistry,
    pCallback: (pImageUri: Uri?) -> Unit
) {
    private val mGetContent: ActivityResultLauncher<String> = pActivityResultRegistry.register(cRegistryKey, ActivityResultContracts.GetContent(), pCallback)

    fun pickImage() {
        mGetContent.launch(cMimetypeImages)
    }
}

@Suppress("DEPRECATION")
fun playStoreIntent(pUrl: String, pPackageName: String): Intent {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(String.format("%s?id=%s", pUrl, pPackageName))
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

fun Context.openWebUrl(pUrl: String) {
    simpleTry {

    }
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pUrl)))
    } catch (e: Exception) {
        makeToast(R.string.something_went_wrong)
    }
}

fun Context.shareApp(pAppId: String, pShareText: String) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        var shareMessage = "\n$pShareText\n\n"
        shareMessage = "${shareMessage}https://play.google.com/store/apps/details?id=$pAppId"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
    } catch (e: Exception) {
        infoLog("UNLIB: Error while creating share app task: $e")
    }
}

fun Context.shareText(pText: CharSequence) {
    try {
        val intentShare = Intent(Intent.ACTION_SEND)
        intentShare.type = "text/plain"
        intentShare.putExtra(Intent.EXTRA_TEXT, pText)
        startActivity(intentShare)
    }
    catch (e: Exception) {
        infoLog("UNLIB: Error while creating share text task: $e")
    }
}

fun Context.shareImageByUri(uri: Uri){
    val fis = FileInputStream(uri.path)
    val bitmap = BitmapFactory.decodeStream(fis)
    fis.close()

    try {
        val file = File("${this.cacheDir}/shareImage.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        val contentUri = FileProvider.getUriForFile(this, this.packageName + ".provider", file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.type = "image/*"
        this.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    } catch (e: FileNotFoundException) {
        infoLog("UNLIB: Error while creating share image by uri $uri task: $e")
    }
}

fun Context.openAstlerInGoogle() {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/dev?id=4948748506238999540")
        )
    )
}