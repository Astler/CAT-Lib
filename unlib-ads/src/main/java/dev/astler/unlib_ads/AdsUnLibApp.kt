package dev.astler.unlib_ads

import com.google.android.gms.ads.MobileAds
import dev.astler.unlib.CatApp

open class AdsUnLibApp : CatApp() {
    private var mAppOpenManager: AppOpenManager? = null

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)

        mAppOpenManager = AppOpenManager(this)
    }
}
