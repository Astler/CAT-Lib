package dev.astler.catlib.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import dev.astler.catlib.helpers.isN
import dev.astler.catlib.constants.PaidPackagePostfix
import java.util.*


val Context.activeLanguage: String
    get() = run {
        val configuration = resources.configuration
        val locale: Locale = if (isN()) {
            configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            configuration.locale
        }

        locale.language
    }




fun Context.getMobileServiceSource(): MobileServicesSource {
    val googleApi = GoogleApiAvailabilityLight.getInstance()
    if (googleApi.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
        return MobileServicesSource.GOOGLE
    }

//    val huaweiApi = HuaweiApiAvailability.getInstance()
//    if (huaweiApi.isHuaweiMobileServicesAvailable(this) == com.huawei.hms.api.ConnectionResult.SUCCESS) {
//        return MobileServicesSource.HMS
//    }

    return MobileServicesSource.NONE
}

enum class MobileServicesSource {
    GOOGLE,
    HMS,
    NONE
}
