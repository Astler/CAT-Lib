package dev.astler.unli.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import dev.astler.unli.preferencesTool

fun log(text: String, additionalName: String = "") {
    Log.i("ForAstler $additionalName", text)
}

@Suppress("DEPRECATION")
fun Context.isOnline(): Boolean {

    val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT < 23) {
        val networkInfo = connMgr.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo?.type == ConnectivityManager.TYPE_MOBILE
    } else {
        val network = connMgr.activeNetwork

        if (network != null) {
            val networkCapabilities = connMgr.getNetworkCapabilities(network)

            if (networkCapabilities != null) {
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            }
        }
    }

    return false
}

fun Context.rateApp() {
    try {
        val rateIntent = rateIntentForUri("market://details")
        startActivity(rateIntent)
    } catch (e: ActivityNotFoundException) {
        val rateIntent =
            rateIntentForUri("https://play.google.com/store/apps/details")
        startActivity(rateIntent)
    }
}

fun Context.moreApps() {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://dev?id=4948748506238999540"))
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/dev?id=4948748506238999540")
        )
        startActivity(intent)
    }
}

fun Context.vibrateOnClick() {
    if (preferencesTool.vibrateOnClick) {
        val vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationTime = 40L
        if (Build.VERSION.SDK_INT >= 26) {
            vibe.vibrate(VibrationEffect.createOneShot(vibrationTime, 10))
        } else {
            vibe.vibrate(vibrationTime)
        }
    }
}