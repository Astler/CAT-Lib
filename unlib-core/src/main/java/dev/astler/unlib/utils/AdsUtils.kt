package dev.astler.unlib.utils

import android.content.Context
import dev.astler.unlib.cNoAdsName
import dev.astler.unlib.gPreferencesTool
import java.util.* // ktlint-disable no-wildcard-imports

fun Context.canShowAds(): Boolean {
    return if (
        applicationContext.packageName.endsWith("paid") ||
        gPreferencesTool.getBoolean(cNoAdsName, false)
    ) {
        false
    } else {
        gPreferencesTool.dayWithoutAds != GregorianCalendar.getInstance().get(
            Calendar.DAY_OF_MONTH
        ) && isOnline()
    }
}
