package dev.astler.unlib.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

@Suppress("DEPRECATION")
fun Context.isOnline(): Boolean {
    val nConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (isNotM()) {
        val networkInfo = nConnectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo?.type == ConnectivityManager.TYPE_MOBILE
    } else {
        val network = nConnectivityManager.activeNetwork

        if (network != null) {
            val networkCapabilities = nConnectivityManager.getNetworkCapabilities(network)

            if (networkCapabilities != null) {
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            }
        }
    }

    return false
}

fun Context.openAppInPlayStore(pPackageName: String = packageName) {
    openPlayStorePage("market://details", "https://play.google.com/store/apps/details", pPackageName)
}

fun Context.openPlayStoreDeveloperPage(pDeveloperId: String = "4948748506238999540") {
    openPlayStorePage("market://dev", "https://play.google.com/store/apps/dev", pDeveloperId)
}

fun Context.openPlayStorePage(pMarketLink: String, pBrowserLink: String, pDataToOpen: String) {
    tryWithParameters(pMarketLink, pBrowserLink) {
        startActivity(playStoreIntent(it, pDataToOpen))
    }
}
