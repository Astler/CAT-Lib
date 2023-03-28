package dev.astler.ads.utils

import android.content.Context
import android.provider.Settings
import com.google.android.gms.ads.AdRequest

fun Context.isGoogleTestDevice(): Boolean {
    val androidId: String =
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    return AdRequest.DEVICE_ID_EMULATOR == androidId
}
