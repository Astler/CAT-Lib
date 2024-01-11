package dev.astler.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import dev.astler.ads.utils.canShowAds
import dev.astler.ads.utils.lastAdsTime
import dev.astler.cat_ui.appResumeTime
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.extensions.now
import dev.astler.catlib.helpers.adsLog
import dev.astler.catlib.preferences.PreferencesTool
import java.util.Date
import javax.inject.Inject


class AppOpenManager @Inject constructor(
    private val _context: Context,
    private val _preferences: PreferencesTool,
    val appConfig: AppConfig,
) : Application.ActivityLifecycleCallbacks, LifecycleEventObserver {

    private val _startAdsKey = ":StartAd"

    private var _appOpenAd: AppOpenAd? = null
    private var _loadCallback: AppOpenAdLoadCallback? = null
    private var _currentActivity: Activity? = null

    private var _loadTime: Long = 0
    private var _isShowingAd = false

    init {
        if (_context is Application) {
            _context.registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        }
        fetchAd()
    }

    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    private val isAdAvailable: Boolean
        get() {
            val timePassed =
                Date().time - _preferences.appResumeTime > adsConfig.startAdDelay * 1000
            val otherAdLastTime = (now - _preferences.lastAdsTime) > adsConfig.startAdOtherAdDelay * 1000L
            val canShowAds = _currentActivity?.canShowAds(_preferences) == true

            log(
                """
                Start Ads report
                
                1) adsConfig.isStartAdsEnabled --> ${adsConfig.isStartAdsEnabled}
                2) has AppOpenAd --> ${_appOpenAd != null}
                3) is NOT first start --> ${!_preferences.isFirstStart}
                4) time from last start ad (${adsConfig.startAdDelay / 60} m) passed? --> $timePassed
                5) time from any other ad (${adsConfig.startAdOtherAdDelay} s) passed? --> $otherAdLastTime
                6) can show ads at all? --> $canShowAds
            """.trimIndent()
            )

            return adsConfig.isStartAdsEnabled &&
                    _appOpenAd != null &&
                    !_preferences.isFirstStart &&
                    timePassed &&
                    otherAdLastTime &&
                    canShowAds
        }

    private fun log(text: String) {
        adsLog(text, postCategory = _startAdsKey)
    }

    private fun showAdIfAvailable() {
        if (_isShowingAd || !isAdAvailable) {
            if (_appOpenAd == null) {
                log("fetchAd")
                fetchAd()
            }

            return
        }

        log("will show ad")

        val fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                _appOpenAd = null
                _isShowingAd = false
                log("ad dismissed")
                fetchAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                log("show ad error: ${adError.message}")
            }

            override fun onAdShowedFullScreenContent() {
                log("showed")
                _isShowingAd = true
                _preferences.lastAdsTime = Date().time
            }
        }

        _appOpenAd?.fullScreenContentCallback = fullScreenContentCallback

        _currentActivity?.let {
            _appOpenAd?.show(it)
        }
    }

    private fun fetchAd() {
        _loadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(pAd: AppOpenAd) {
                _appOpenAd = pAd
                _loadTime = Date().time
            }

            override fun onAdFailedToLoad(pLoadAdError: LoadAdError) {
                log("load ad error = ${pLoadAdError.message}")
            }
        }

        AppOpenAd.load(
            _context,
            appConfig.startAdId,
            adRequest,
            _loadCallback as AppOpenAdLoadCallback
        )
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            showAdIfAvailable()
        }
    }

    override fun onActivityStarted(pActivity: Activity) {
        _currentActivity = pActivity
    }

    override fun onActivityResumed(pActivity: Activity) {
        _currentActivity = pActivity
    }

    override fun onActivityDestroyed(pActivity: Activity) {
        _currentActivity = null
    }

    override fun onActivityPaused(pActivity: Activity) {}

    override fun onActivityCreated(pActivity: Activity, p1: Bundle?) {}

    override fun onActivityStopped(pActivity: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
}
