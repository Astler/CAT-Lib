package dev.astler.catlib.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import dev.astler.catlib.core.R
import dev.astler.catlib.gPreferencesTool
import java.util.*

class ContextUtils(base: Context?) : ContextWrapper(base)

fun Context.isDebuggable(): Boolean {
    return (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}

fun Context.formattedPackageName(): String = packageName?.replace(".", "_") ?: "null_package"

fun Context.copyToBuffer(pData: CharSequence) {
    if (pData.isNotEmpty()) {
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", pData)
        myClipboard.setPrimaryClip(myClip)
        toast(getString(R.string.copied_to_buffer, pData))
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
    return tryWithDefault(false) {
        packageManager.getPackageInfo(packageName, 0)
        true
    }
}

fun Context.isPackageInstalledAlt(packageName: String): Boolean {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    return intent != null
}

private const val GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending"

fun Context.isPlayStoreInstalled(): Boolean {
    return try {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                GOOGLE_PLAY_STORE_PACKAGE,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            packageManager.getPackageInfo(GOOGLE_PLAY_STORE_PACKAGE, 0)
        }

        val installerPackageName = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            packageManager.getInstallerPackageName(packageInfo.packageName)
        } else {
            packageManager.getInstallSourceInfo(packageInfo.packageName).installingPackageName
        }

        val isInstalled =
            packageInfo.applicationInfo.enabled && GOOGLE_PLAY_STORE_PACKAGE == installerPackageName

        infoLog("Play Store is installed: $isInstalled", "Play Store")

        isInstalled
    } catch (exc: PackageManager.NameNotFoundException) {
        errorLog("Play Store not installed")
        false
    } catch (exc: Exception) {
        errorLog("Error while checking Play Store installation: ${exc.localizedMessage}")
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
