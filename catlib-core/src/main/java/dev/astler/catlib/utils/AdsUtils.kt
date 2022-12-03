package dev.astler.catlib.utils

import android.content.Context
import dev.astler.catlib.gPreferencesTool
import java.util.*

fun Context.canShowAds(): Boolean {
    return if (
        applicationContext.isPaidVersion() || gPreferencesTool.adsDisabled
    ) {
        false
    } else {
        gPreferencesTool.noAdsDay != GregorianCalendar.getInstance().get(
            Calendar.DAY_OF_MONTH
        ) && isOnline()
    }
}
