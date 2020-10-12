package dev.astler.unli.utils

import android.content.Context
import dev.astler.unli.preferencesTool
import java.util.*

fun Context.canShowAds(): Boolean {
    return if (applicationContext.packageName.endsWith("paid")) {
        false
    }
    else {
        preferencesTool.dayWithoutAds != GregorianCalendar.getInstance().get(
                Calendar.DAY_OF_MONTH) && isOnline()
    }
}
