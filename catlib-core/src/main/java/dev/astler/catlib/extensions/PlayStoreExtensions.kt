package dev.astler.catlib.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.utils.playStoreIntent
import dev.astler.catlib.helpers.trackedTry

private const val GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending"

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

