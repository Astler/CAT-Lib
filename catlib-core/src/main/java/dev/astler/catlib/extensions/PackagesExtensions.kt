package dev.astler.catlib.extensions

import android.content.Context
import dev.astler.catlib.helpers.trackedTry

fun Context.packageId(): String = packageName?.replace(".", "_") ?: "null_package"

fun Context.shortPackageId(): String {
    val parts = (packageName ?: "null_package").split(".")

    val builder = StringBuilder()

    for (part in parts) {
        if (parts.last() == part) {
            builder.append(part.substring(0, if (part.length < 3) part.length else 3))
            break
        }

        part.firstOrNull()?.let {
            builder.append(it)
        }
    }

    return builder.toString()
}

fun Context.isPackageInstalled(packageName: String): Boolean {
    return trackedTry(fallbackValue = false) {
        packageManager.getPackageInfo(packageName, 0)
        true
    }
}

fun Context.isPackageInstalledAlt(packageName: String): Boolean {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    return intent != null
}