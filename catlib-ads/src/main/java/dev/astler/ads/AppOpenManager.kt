package dev.astler.ads

import android.app.Activity
import android.app.Application
import android.content.Context
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
import dev.astler.catlib.gAppConfig
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.utils.adsLog
import dev.astler.catlib.utils.canShowAds
import dev.astler.ads.utils.isRewardAdIsActive
import java.util.*
import javax.inject.Inject

class AppOpenManager @Inject constructor(
    applicationContext: Context,
    preferences: PreferencesTool,
) :
    Application.ActivityLifecycleCallbacks,
    LifecycleObserver {
    private var _context: Context
    private var _preferences: PreferencesTool
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback? = null
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0
    private var mLastShowTime: Long = 0
    private var isShowingAd = false

    init {
        _context = applicationContext
        _preferences = preferences

        if (applicationContext is Application) {
            applicationContext.registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        }

        mLastShowTime = preferences.getLong("start_ad_timer", 0)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
    }

    private fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable) {
            adsLog("START AD: Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        adsLog("START AD: Error ${adError.message}.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                        mLastShowTime = Date().time
                        _preferences.edit("start_ad_timer", mLastShowTime)
                    }
                }

            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback

            currentActivity?.let {
                appOpenAd?.show(it)
            }
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
                adsLog("START AD: load ad error = ${pLoadAdError.message}")
            }
        }

        AppOpenAd.load(
            _context, gAppConfig.mStartAdId, adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback as AppOpenAdLoadCallback
        )
    }

    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    /** Utility method that checks if ad exists and can be shown.  */
    private val isAdAvailable: Boolean
        get() {
            adsLog("START AD: Full check")
            adsLog("START AD: appOpenAd != null --> ${appOpenAd != null}")
            adsLog("START AD: wasLoadTimeLessThanNHoursAgo(4) --> ${wasLoadTimeLessThanNHoursAgo(4)}")
            adsLog("START AD: !gPreferencesTool.isFirstStart --> ${!_preferences.isFirstStart}")
            adsLog("START AD: Date().time - mLastShowTime > 3600000 --> ${Date().time - mLastShowTime > 1800000}")

            return appOpenAd != null &&
                    wasLoadTimeLessThanNHoursAgo(4) &&
                    !_preferences.isFirstStart &&
                    Date().time - mLastShowTime > 1800000 &&
                    Date().time - _preferences.getLong("last_ad_show") > 25000 &&
                    !_preferences.isRewardAdIsActive() &&
                    currentActivity?.canShowAds() == true
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
