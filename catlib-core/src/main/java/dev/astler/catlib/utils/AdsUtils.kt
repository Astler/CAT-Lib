package dev.astler.catlib.utils

import android.content.Context
import dev.astler.catlib.preferences.PreferencesTool
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Can be used to detect are we can show ads or not. It depends on paid version, ads disabled and no ads day.
 */
fun Context.canShowAds(preferences: PreferencesTool): Boolean {
    return if (
        applicationContext.isPaidVersion || preferences.adsDisabled
    ) {
        false
    } else {
        preferences.noAdsDay != GregorianCalendar.getInstance().get(
            Calendar.DAY_OF_MONTH
        ) && isOnline
    }
}
