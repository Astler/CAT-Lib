package dev.astler.unlib_ads.utils

import android.content.Context
import dev.astler.unlib.UnliApp
import dev.astler.unlib.utils.isOnline
import java.util.* // ktlint-disable no-wildcard-imports

fun Context.canShowAds(): Boolean {
    return if (
        applicationContext.packageName.endsWith("paid") ||
        UnliApp.getInstance().mAdsDisabled
    ) {
        false
    } else {
        UnliApp.getInstance().mNoAdsDay != GregorianCalendar.getInstance().get(
            Calendar.DAY_OF_MONTH
        ) && isOnline()
    }
}
