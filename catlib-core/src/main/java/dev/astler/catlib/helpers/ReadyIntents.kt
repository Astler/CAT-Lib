package dev.astler.catlib.helpers

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import dev.astler.catlib.core.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Suppress("DEPRECATION")
fun playStoreIntent(url: String, packageName: String): Intent {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(String.format("%s?id=%s", url, packageName))
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
    trackedTry {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pUrl)))
    }
}

fun Context.shareApp(appName: String, pAppId: String, pShareText: String) {
    trackedTry {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName)
        var shareMessage = "\n$pShareText\n\n"
        shareMessage = "${shareMessage}https://play.google.com/store/apps/details?id=$pAppId"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, getString(gg.pressf.resources.R.string.share)))
    }
}

fun Context.shareText(pText: CharSequence) {
    trackedTry {
        startActivity(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, pText)
            }
        )
    }
}

fun Context.shareImageByUri(uri: Uri) {
    val fis = FileInputStream(uri.path)
    val bitmap = BitmapFactory.decodeStream(fis)
    fis.close()

    trackedTry {
        val file = File("${this.cacheDir}/shareImage.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        val contentUri = FileProvider.getUriForFile(this, this.packageName + ".provider", file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.type = "image/*"
        this.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }
}

fun Context.openAstlerInGoogle() = openWebUrl("https://play.google.com/store/apps/dev?id=4948748506238999540")
