package dev.astler.unlib.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import dev.astler.unlib.core.R
import dev.astler.unlib.gPreferencesTool
import java.util.* // ktlint-disable no-wildcard-imports

class ContextUtils(base: Context?) : ContextWrapper(base)

@Suppress("DEPRECATION")
fun updateLocale(pContext: Context, localeToSwitchTo: Locale?): ContextWrapper {
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

fun Context.copyToBuffer(pData: CharSequence, pToast: Toast?): Toast {
    val nToast = makeToast(getString(R.string.copied_to_buffer, pData), pToast = pToast, pShowToast = false)

    if (pData.isNotEmpty()) {
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", pData)
        myClipboard.setPrimaryClip(myClip)

        nToast.show()
    }

    return nToast
}

fun Context.copyToBuffer(pData: CharSequence) {
    if (pData.isNotEmpty()) {
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", pData)
        myClipboard.setPrimaryClip(myClip)
        makeToast(getString(R.string.copied_to_buffer, pData))
    }
}

fun Context.readFromBuffer(): CharSequence {
    val pClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    return if (pClipboard.primaryClip != null) {
        val item = pClipboard.primaryClip?.getItemAt(0)
        item?.text ?: ""
    } else {
        ""
    }
}

fun Context.vibrateOnClick() {
    if (gPreferencesTool.vibrateOnClick) {
        vibrate()
    }
}

@Suppress("DEPRECATION")
fun Context.vibrate(pVibrationTime: Long = 40L) {
    val vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibe.vibrate(VibrationEffect.createOneShot(pVibrationTime, 10))
    } else {
        vibe.vibrate(pVibrationTime)
    }
}

fun Context.isPackageInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.getMobileServiceSource(): MobileServicesSource {
    val googleApi = GoogleApiAvailabilityLight.getInstance()
    if (googleApi.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
        return MobileServicesSource.GOOGLE
    }

//    val huaweiApi = HuaweiApiAvailability.getInstance()
//    if (huaweiApi.isHuaweiMobileServicesAvailable(this) == com.huawei.hms.api.ConnectionResult.SUCCESS) {
//        return MobileServicesSource.HMS
//    }

    return MobileServicesSource.NONE
}

enum class MobileServicesSource {
    GOOGLE,
    HMS,
    NONE
}
