package dev.astler.ads.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.google.android.gms.ads.AdRequest
import dev.astler.catlib.extensions.isOnline
import dev.astler.catlib.extensions.isPaidVersion
import dev.astler.catlib.preferences.PreferencesTool
import java.util.Calendar
import java.util.GregorianCalendar

fun isEmulator(): Boolean {
    val fingerprint = Build.FINGERPRINT
    return (fingerprint.startsWith("generic")
            || fingerprint.startsWith("unknown")
            || fingerprint.contains("test-keys"))
}

fun Context.isGoogleTestDevice(): Boolean {
    val androidId: String =
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    return AdRequest.DEVICE_ID_EMULATOR == androidId
}

fun Context.canShowAds(preferences: PreferencesTool): Boolean {
    return if (
        applicationContext.isPaidVersion || preferences.adsDisabled
    ) {
        false
    } else {
        val currentTime = System.currentTimeMillis()
        val oneHourInMillis = 3600000L
        return currentTime - preferences.noAdsStartTime > oneHourInMillis && isOnline
    }
}
