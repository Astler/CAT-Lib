package dev.astler.unlib_ads.activity

import com.google.android.gms.ads.MobileAds
import dev.astler.unlib.UnliApp
import dev.astler.unlib_ads.AppOpenManager

open class AdsUnLibApp: UnliApp() {
    private var mAppOpenManager: AppOpenManager? = null

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)

        mAppOpenManager = AppOpenManager(this)
    }
}