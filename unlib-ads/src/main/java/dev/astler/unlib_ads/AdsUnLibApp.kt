package dev.astler.unlib_ads

import com.google.android.gms.ads.MobileAds
import dev.astler.unlib.UnliApp

open class AdsUnLibApp : UnliApp() {
    private var mAppOpenManager: AppOpenManager? = null

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)

        mAppOpenManager = AppOpenManager(this)
    }
}
