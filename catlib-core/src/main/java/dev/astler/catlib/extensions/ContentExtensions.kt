package dev.astler.catlib.extensions

import android.content.Context
import android.content.pm.ApplicationInfo
import dev.astler.catlib.constants.PaidPackagePostfix

//TODO create pro interface to validate paid version
val Context.isPaidVersion get() = applicationContext.packageName.endsWith(PaidPackagePostfix)

fun Context.isDebuggable(): Boolean {
    return (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}
