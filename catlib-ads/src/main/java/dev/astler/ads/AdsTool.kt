package dev.astler.ads

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.devtodev.analytics.internal.services.abtests.add
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import dev.astler.ads.data.RemoteConfigData
import dev.astler.ads.dialogs.adsAgeConfirmDialog
import dev.astler.ads.interfaces.IAdToolProvider
import dev.astler.ads.utils.NativeAdsLoader
import dev.astler.ads.utils.ageConfirmed
import dev.astler.ads.utils.canShowAds
import dev.astler.ads.utils.childAdsMode
import dev.astler.ads.utils.lastAdsTime
import dev.astler.ui.appResumeTime
import dev.astler.ui.utils.views.showWithCondition
import dev.astler.catlib.ads.databinding.ItemAdBinding
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.extensions.now
import dev.astler.catlib.extensions.shortPackageId
import dev.astler.catlib.helpers.adsLog
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.RemoteConfigProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.GregorianCalendar
import javax.inject.Inject

private var _adsConfig = RemoteConfigData()
val adsConfig get() = _adsConfig

class AdsTool @Inject constructor(
    private val _context: Context,
    val preferences: PreferencesTool,
    private val remoteConfig: RemoteConfigProvider,
    val appConfig: AppConfig
) : SharedPreferences.OnSharedPreferenceChangeListener {

    private var _interstitialRequestTimerActive: Boolean = false
    private var _rewardedRequestTimerActive: Boolean = false
    private var _configPackageName: String = _context.shortPackageId()
    private var _needAgeCheck: Boolean = appConfig.ageRestricted

    private var _interstitialAdId = appConfig.interstitialAdId
    private var _rewardedAdId = appConfig.rewardedAdId

    private var _loadedRewardedInterstitial: RewardedInterstitialAd? = null
    private var _loadedInterstitial: InterstitialAd? = null

    val canShowAds get() = _context.canShowAds(preferences)

    init {
        adsLog("AdsTool init started")

        if (_context is AppCompatActivity) {
            val lifecycleOwner = _context as LifecycleOwner
            val lifecycleScope = _context.lifecycleScope

            lifecycleScope.launch {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    adsLog("AdsTool entered created state")
                    initializeAds()
                    adsLog("AdsTool init done!")
                }
            }

            preferences.addListener(this)

            fetchRemoteConfigForAds()
        }
    }

    private fun initializeAds() {
        fun setupMobileAds() {
            val requestConfiguration =
                RequestConfiguration.Builder().setTestDeviceIds(getTestDevicesList())

            if (preferences.childAdsMode) {
                requestConfiguration.setTagForChildDirectedTreatment(
                    RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
                )
            }

            MobileAds.setRequestConfiguration(requestConfiguration.build())

            loadAds()
        }

        if (appConfig.nativeAdId.isNotEmpty()) {
            startNativeAdsLoader()
        }

        if (!preferences.ageConfirmed && _needAgeCheck) {
            _context.adsAgeConfirmDialog {
                preferences.childAdsMode = false
                setupMobileAds()
            }

            preferences.ageConfirmed = true
        } else {
            setupMobileAds()
        }
    }

    public val noAdsRewardListener: OnUserEarnedRewardListener
        get() = OnUserEarnedRewardListener {
            preferences.noAdsStartTime = System.currentTimeMillis()
        }

    private fun fetchRemoteConfigForAds() {
        remoteConfig.loadRemoteData {
            _adsConfig = RemoteConfigData(remoteConfig, _configPackageName)
            adsLog("loaded ads config $_adsConfig")
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PreferencesTool.noAdsStartTimeKey) {
            if (_context is IAdToolProvider) {
                _context.hideAd()
            }
        }
    }

    fun startNativeAdsLoader() {
        if (NativeAdsLoader.instance == null) {
            NativeAdsLoader(appConfig)
        }

        NativeAdsLoader.instance?.loadAds(_context, AdRequest.Builder().build())
    }

    fun tryToShowRewardedInterstitial(rewardListener: OnUserEarnedRewardListener = noAdsRewardListener) {
        if (_context !is AppCompatActivity) {
            adsLog("Cant show ads, context is not AppCompatActivity")
            return
        }

        if (_loadedRewardedInterstitial != null) {
            _loadedRewardedInterstitial?.fullScreenContentCallback
            _loadedRewardedInterstitial?.show(_context, rewardListener)
        } else {
            loadAds()
        }
    }

    fun createNativeAdLoader(pAdBindItem: ItemAdBinding): AdLoader {
        var adLoader: AdLoader? = null

        adsLog("Native Ad Started")

        adLoader =
            AdLoader.Builder(_context, appConfig.nativeAdId).forNativeAd { nativeAd: NativeAd ->
                if (adLoader?.isLoading == true) {
                    adsLog("Native Ad Banner is loading")
                } else {
                    adsLog("Native Ad Banner is loaded")
                    nativeAd.setupNativeBanner(pAdBindItem)
                }

                if (_context is AppCompatActivity) {
                    if (_context.isDestroyed) {
                        nativeAd.destroy()
                        return@forNativeAd
                    }
                }

            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    errorLog("error = $adError")
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()

        adsLog("Native Ad Builded")

        adLoader.loadAds(AdRequest.Builder().build(), 5)

        return adLoader
    }

    fun tryToShowInterstitialAd() {
        if (!canShowAds) {
            adsLog("Can't show ads!")
            return
        }

        if (_context !is AppCompatActivity) {
            adsLog("Cant show ads, context is not AppCompatActivity")
            return
        }

        if (_loadedInterstitial == null) {
            loadAds()
            return
        }

        if (_adsConfig.isEmpty) {
            adsLog("isEmpty need to fetch")
            fetchRemoteConfigForAds()
            return
        }

        val config = _adsConfig

        if (!config.interstitialAdEnabled) {
            adsLog("Ads disabled in remote config!")
            return
        }

        val startDelay = config.interstitialAdDelay

        if ((now - preferences.appResumeTime) < startDelay * 1000L) {
            adsLog("Time from start not passed ${(preferences.appResumeTime - GregorianCalendar().timeInMillis) / 1000}!")
            return
        }

        if ((now - preferences.lastAdsTime) < config.interstitialAdOtherDelay * 1000L) {
            adsLog("Time from last ad not passed ${(preferences.lastAdsTime - GregorianCalendar().timeInMillis) / 1000}!")
            return
        }

        _loadedInterstitial?.show(_context)
        preferences.lastAdsTime = GregorianCalendar().timeInMillis
    }

    private fun loadAds() {
        if (!canShowAds) {
            adsLog("Can't show ads by preferences!", "AdsTool")
            return
        }

        val adRequest = AdRequest.Builder().build()

        tryToLoadRewardedInterstitial(adRequest)
        tryToLoadInterstitial(adRequest)
    }

    private fun tryToLoadInterstitial(adRequest: AdRequest) {
        if (_interstitialRequestTimerActive) return
        adsLog("Try to load interstitial", "AdsTool")
        if (_interstitialAdId.isEmpty() || _loadedInterstitial != null) return

        fun onAdPresenter() {
            _loadedInterstitial = null
            tryToLoadInterstitial(adRequest)
        }

        adsLog("Load interstitial")

        InterstitialAd.load(_context,
            _interstitialAdId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    _loadedInterstitial = interstitialAd

                    adsLog("Interstitial loaded!")

                    _loadedInterstitial?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                onAdPresenter()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                onAdPresenter()
                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                onAdPresenter()
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    adsLog("Interstitial failed to load: ${loadAdError.message}")
                    _loadedInterstitial = null

                    if (_context is AppCompatActivity) {
                        adsLog("Interstitial started reload timer")
                        _interstitialRequestTimerActive = true
                        _context.lifecycleScope.launch {
                            delay(5000)
                            _interstitialRequestTimerActive = false
                            onAdPresenter()
                        }
                    }
                }
            })
    }

    private fun tryToLoadRewardedInterstitial(adRequest: AdRequest) {
        if (_rewardedRequestTimerActive) return
        adsLog("Try to load rewarded interstitial")

        if (_rewardedAdId.isEmpty() || _loadedRewardedInterstitial != null) return

        fun onAdPresenter(isSuccess: Boolean) {
            _loadedRewardedInterstitial = null
            if (isSuccess) return
            tryToLoadRewardedInterstitial(adRequest)
        }

        adsLog("Load rewarded interstitial")

        RewardedInterstitialAd.load(_context,
            _rewardedAdId,
            adRequest,
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    _loadedRewardedInterstitial = ad

                    adsLog("Rewarded interstitial loaded!")

                    _loadedRewardedInterstitial?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                onAdPresenter(false)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                onAdPresenter(false)
                                adsLog("Rewarded interstitial failed to show: ${adError.message}")
                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                onAdPresenter(true)
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    adsLog("Rewarded interstitial failed to load: ${loadAdError.message}")
                    if (_context is AppCompatActivity) {
                        adsLog("Rewarded interstitial started reload timer")
                        _rewardedRequestTimerActive = true
                        _context.lifecycleScope.launch {
                            delay(5000)
                            _rewardedRequestTimerActive = false
                            onAdPresenter(false)
                        }
                    }
                }
            })
    }

    private fun NativeAd.setupNativeBanner(binding: ItemAdBinding) {
        with(binding) {
            adHeadline.text = headline
            adBody.text = body

            install.showWithCondition(callToAction != null)
            install.text = callToAction

            showWithCondition(icon == null, adAppIcon, adAppIconCard)

            icon?.drawable?.let {
                adAppIcon.setImageDrawable(it)
            }

            nativeAd.apply {
                headlineView = adHeadline
                bodyView = adBody
                callToActionView = install
            }

            nativeAd.setNativeAd(this@setupNativeBanner)
        }
    }

    @SuppressLint("VisibleForTests")
    private fun getTestDevicesList() = appConfig.testDevices.add(AdRequest.DEVICE_ID_EMULATOR)
}