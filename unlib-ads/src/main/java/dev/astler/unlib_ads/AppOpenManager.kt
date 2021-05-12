package dev.astler.unlib_ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import dev.astler.unlib.gAppConfig
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.utils.canShowAds
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib_ads.activity.AdsUnLibApp
import java.util.*

class AppOpenManager(myApplication: AdsUnLibApp) : Application.ActivityLifecycleCallbacks,
    LifecycleObserver {
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback? = null
    private val mApplication: AdsUnLibApp = myApplication
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0
    private var mLastShowTime: Long = 0
    private var isShowingAd = false

    init {
        this.mApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        mLastShowTime = gPreferencesTool.getLong("start_ad_timer", 0)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
    }

    private fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable) {
            infoLog("START AD: Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        infoLog("START AD: Error ${adError.message}.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                        mLastShowTime = Date().time
                        gPreferencesTool.edit("start_ad_timer", mLastShowTime)
                    }
                }

            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback

            appOpenAd?.show(currentActivity)
        } else {
            fetchAd()
        }
    }

    fun fetchAd() {
        if (isAdAvailable) {
            return
        }

        loadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(pAd: AppOpenAd) {
                appOpenAd = pAd
                loadTime = Date().time
            }

            override fun onAdFailedToLoad(pLoadAdError: LoadAdError) {
                infoLog("START AD: load ad error = ${pLoadAdError.message}")
            }
        }

        AppOpenAd.load(
            mApplication, gAppConfig.mStartAdId, adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback
        )
    }

    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    /** Utility method that checks if ad exists and can be shown.  */
    private val isAdAvailable: Boolean
        get() {
//            infoLog("START AD: Full check")
//            infoLog("START AD: appOpenAd != null --> ${appOpenAd != null}")
//            infoLog("START AD: wasLoadTimeLessThanNHoursAgo(4) --> ${wasLoadTimeLessThanNHoursAgo(4)}")
//            infoLog("START AD: !gPreferencesTool.isFirstStart --> ${!gPreferencesTool.isFirstStart}")
//            infoLog("START AD: Date().time - mLastShowTime > 3600000 --> ${Date().time - mLastShowTime > 1800000}")
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4) && !gPreferencesTool.isFirstStart && Date().time - mLastShowTime > 1800000 && currentActivity?.canShowAds() == true
        }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    override fun onActivityCreated(pActivity: Activity, p1: Bundle?) {}

    override fun onActivityStarted(pActivity: Activity) {
        currentActivity = pActivity
    }

    override fun onActivityResumed(pActivity: Activity) {
        currentActivity = pActivity
    }

    override fun onActivityPaused(pActivity: Activity) {}

    override fun onActivityStopped(pActivity: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(pActivity: Activity) {
        currentActivity = null
    }

}