package dev.astler.catlib.utils

import android.content.Context

fun Context.openAppInPlayStore(pPackageName: String = packageName) {
    openPlayStorePage("market://details", "https://play.google.com/store/apps/details", pPackageName)
}

fun Context.openPlayStoreDeveloperPage(pDeveloperId: String = "4948748506238999540") {
    openPlayStorePage("market://dev", "https://play.google.com/store/apps/dev", pDeveloperId)
}

fun Context.openPlayStorePage(pMarketLink: String, pBrowserLink: String, pDataToOpen: String) {
    trackedTry {
        startActivity(playStoreIntent(pMarketLink, pDataToOpen))
    }
}
