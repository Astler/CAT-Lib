package dev.astler.catlib.helpers

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import dev.astler.catlib.core.R
import dev.astler.catlib.extensions.toast

fun Context.sendEmail(recipient: String, subject: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO, "mailto:".toUri())
    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

    try {
        startActivity(Intent.createChooser(emailIntent, "Choose Email Client..."))
    }
    catch (e: Exception){
        toast(gg.pressf.resources.R.string.no_app_to_send_email)
    }
}