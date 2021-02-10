package dev.astler.unlib.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build

import android.os.LocaleList
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import dev.astler.unlib.core.R
import dev.astler.unlib.gPreferencesTool
import java.util.*

class ContextUtils(base: Context?) : ContextWrapper(base)

fun updateLocale(
    pContext: Context,
    localeToSwitchTo: Locale?
): ContextWrapper? {
    var context = pContext
    val resources: Resources = context.resources
    val configuration: Configuration = resources.configuration // 1

    infoLog("this locale is = ${localeToSwitchTo?.language}")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val localeList = LocaleList(localeToSwitchTo) // 2
        LocaleList.setDefault(localeList) // 3
        configuration.setLocales(localeList) // 4
    } else {
        configuration.locale = localeToSwitchTo // 5
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        context = context.createConfigurationContext(configuration) // 6
    } else {
        resources.updateConfiguration(configuration, resources.displayMetrics) // 7
    }
    return ContextUtils(context)
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

fun Context.vibrateOnClick() {
    if (gPreferencesTool.vibrateOnClick) {
        val vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationTime = 40L
        if (Build.VERSION.SDK_INT >= 26) {
            vibe.vibrate(VibrationEffect.createOneShot(vibrationTime, 10))
        } else {
            vibe.vibrate(vibrationTime)
        }
    }
}