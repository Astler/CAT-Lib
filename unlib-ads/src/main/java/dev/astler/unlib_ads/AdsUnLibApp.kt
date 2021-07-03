package dev.astler.unlib_ads

import com.google.android.gms.ads.MobileAds
import dev.astler.unlib.LocalStorage
import dev.astler.unlib.UnliApp
import kotlinx.coroutines.launch

open class AdsUnLibApp : UnliApp() {

    private var mAppOpenManager: AppOpenManager? = null

    override var mAdsDisabled = false

    override fun onCreate() {
        super.onCreate()

        mApplicationScope.launch {
            LocalStorage.noAdsDayWatcher { mNoAdsDay = it }
            mAdsDisabled = LocalStorage.adsDisabled()
        }

        MobileAds.initialize(this)

        mAppOpenManager = AppOpenManager(this)
    }
}
