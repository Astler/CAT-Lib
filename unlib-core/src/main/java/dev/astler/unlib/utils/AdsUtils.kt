package dev.astler.unlib.utils

import android.content.Context
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.paidPackagePostfix
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
