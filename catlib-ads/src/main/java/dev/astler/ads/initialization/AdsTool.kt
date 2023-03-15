package dev.astler.ads.initialization

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
import dev.astler.ads.data.adChanceKey
import dev.astler.ads.data.bannerAdEnabledKey
import dev.astler.ads.data.interstitialAdEnabledKey
import dev.astler.ads.data.lastAdDelayKey
import dev.astler.ads.data.openAdEnabledKey
import dev.astler.ads.data.resumeAdDelayKey
import dev.astler.ads.data.rewardAdEnabledKey
import dev.astler.ads.data.startAdDelayKey
import dev.astler.ads.dialogs.adsAgeConfirmDialog
import dev.astler.ads.interfaces.IAdListener
import dev.astler.ads.utils.NativeAdsLoader
import dev.astler.ads.utils.ageConfirmed
import dev.astler.ads.utils.childAdsMode
import dev.astler.ads.utils.lastAdsTime
import dev.astler.ads.utils.rewardAdActive
import dev.astler.cat_ui.ResumeTimeKey
import dev.astler.cat_ui.StartTimeKey
import dev.astler.cat_ui.utils.views.goneView
import dev.astler.cat_ui.utils.views.showView
import dev.astler.catlib.gAppConfig
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.RemoteConfigProvider
import dev.astler.catlib.utils.adsLog
import dev.astler.catlib.utils.canShowAds
import dev.astler.catlib.utils.formattedPackageName
import dev.astler.catlib.utils.hasPrefsTimePassed
import dev.astler.catlib.utils.infoLog
import dev.astler.catlib.ads.databinding.ItemAdBinding
import java.util.GregorianCalendar
import javax.inject.Inject

var _adsConfig = RemoteConfigData()
val adsConfig get() = _adsConfig

class AdsTool @Inject constructor(
    val context: Context,
    val preferences: PreferencesTool,
    private val remoteConfig: RemoteConfigProvider
) :
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var _configPackageName: String = context.formattedPackageName()
    private var _needAgeCheck: Boolean = gAppConfig.mNeedAgeCheck

    private var _interstitialAdId = gAppConfig.mInterstitialAdId
    private var _rewardedAdId = gAppConfig.mRewardedAdId

    private var _loadedRewardedInterstitial: RewardedInterstitialAd? = null
    private var _loadedInterstitial: InterstitialAd? = null

    init {
        if (context is AppCompatActivity) {
            context.lifecycleScope.launchWhenResumed {
                initializeAds()
            }

            preferences.addListener(this)

            fetchRemoteConfigForAds()
        }
    }

    private val _noAdsRewardListener: OnUserEarnedRewardListener
        get() = OnUserEarnedRewardListener {
            preferences.noAdsDay = GregorianCalendar.getInstance().get(GregorianCalendar.DATE)
            preferences.rewardAdActive = false
        }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PreferencesTool.dayWithoutAdsKey) {
            if (context is IAdListener) {
                context.hideAd()
            }
        }
    }

    fun startNativeAdsLoader() {
        NativeAdsLoader.instance?.loadAds(context, AdRequest.Builder().build())
    }
    fun showRewardAd() {
        if (context !is AppCompatActivity) {
            adsLog("Cant show ads, context is not AppCompatActivity")
            return
        }

        if (_loadedRewardedInterstitial != null) {
            _loadedRewardedInterstitial?.show(context, _noAdsRewardListener)
            preferences.rewardAdActive = true
        } else {
            requestNewRewardedInterstitial()
        }
    }

    fun tryToShowInterstitial() {
        if (!context.canShowAds()) {
            adsLog("Can't show ads!")
            return
        }
        
        if (_adsConfig == null) {
            fetchRemoteConfigForAds()
            return
        }

        val config = _adsConfig!!

        adsLog("Loaded ads remote config = $config")

        val adsAllowedByConfig = config.interstitialAdEnabled

        if (!adsAllowedByConfig) {
            adsLog("Ads disabled in remote config!")
            return
        }

        val startDelay = config.startAdDelay

        if (!StartTimeKey.hasPrefsTimePassed(startDelay * 1000L)) {
            adsLog("Time from start not passed!")
            return
        }

        val resumeDelay = config.resumeAdDelay

        if (!ResumeTimeKey.hasPrefsTimePassed(resumeDelay * 1000L)) {
            adsLog("Time from resume not passed!")
            return
        }

        val fromLastDelay = config.lastAdDelay

        if (!preferences.lastAdsTime.hasPrefsTimePassed(fromLastDelay * 1000L)) {
            adsLog("Time from last ads not passed!")
            return
        }

        val adChance = config.adChance

        if (adChance > 0) {
            val random = (0..100).random()

            if (random > adChance) {
                adsLog("Random chance not passed!")
                return
            }
        }

        showInterstitialAd()
    }

    fun createNativeAdLoader(pAdBindItem: ItemAdBinding): AdLoader {
        var nAdLoader: AdLoader? = null

        nAdLoader = AdLoader.Builder(context, gAppConfig.mNativeAdId)
            .forNativeAd { nativeAd: NativeAd ->
                if (nAdLoader?.isLoading == true) {
                    adsLog("Native Ad Banner is loading")
                } else {
                    adsLog("Native Ad Banner is loaded")
                    nativeAd.setupNativeBanner(pAdBindItem)
                }

                if (context is AppCompatActivity) {
                    if (context.isDestroyed) {
                        nativeAd.destroy()
                        return@forNativeAd
                    }
                }

            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    infoLog("error = $adError")
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        return nAdLoader
    }

    private fun initializeAds() {
        fun setupMobileAds() {
            val requestConfiguration = RequestConfiguration.Builder()
                .setTestDeviceIds(getTestDevicesList())

            if (preferences.childAdsMode) {
                requestConfiguration.setTagForChildDirectedTreatment(
                    RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
                )
            }

            MobileAds.setRequestConfiguration(requestConfiguration.build())

            loadAd()
        }

        if (!preferences.ageConfirmed && _needAgeCheck) {
            context.adsAgeConfirmDialog {
                preferences.childAdsMode = false
                setupMobileAds()
            }

            preferences.ageConfirmed = true
        } else {
            setupMobileAds()
        }
    }

    private fun fetchRemoteConfigForAds() {
        remoteConfig.loadRemoteData {
            _adsConfig = RemoteConfigData(
                remoteConfig.getLong(startAdDelayKey + _configPackageName),
                remoteConfig.getLong(resumeAdDelayKey + _configPackageName),
                remoteConfig.getLong(lastAdDelayKey + _configPackageName),
                remoteConfig.getLong(adChanceKey + _configPackageName),
                remoteConfig.getBoolean(interstitialAdEnabledKey + _configPackageName),
                remoteConfig.getBoolean(openAdEnabledKey + _configPackageName),
                remoteConfig.getBoolean(bannerAdEnabledKey + _configPackageName),
                remoteConfig.getBoolean(rewardAdEnabledKey + _configPackageName),
            )

            adsLog("loaded ads config $_adsConfig")
        }
    }

    private fun showInterstitialAd() {
        if (context !is AppCompatActivity) {
            adsLog("Cant show ads, context is not AppCompatActivity")
            return
        }

        if (_loadedInterstitial != null) {
            _loadedInterstitial?.show(context)
            preferences.lastAdsTime = GregorianCalendar().timeInMillis
        } else {
            requestNewInterstitial()
        }
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        if (context.canShowAds() && _rewardedAdId.isNotEmpty() && _loadedRewardedInterstitial == null) {
            RewardedInterstitialAd.load(
                context,
                _rewardedAdId,
                adRequest,
                object : RewardedInterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedInterstitialAd) {
                        _loadedRewardedInterstitial = ad

                        infoLog("mRewardedInterstitialAd onAdLoaded", "ForAstler: ADS")
                        _loadedRewardedInterstitial?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    _loadedRewardedInterstitial = null
                                    preferences.rewardAdActive = false
                                    requestNewInterstitial()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    super.onAdFailedToShowFullScreenContent(adError)
                                    _loadedRewardedInterstitial = null
                                    preferences.rewardAdActive = false
                                    requestNewRewardedInterstitial()
                                }

                                override fun onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent()
                                    _loadedRewardedInterstitial = null
                                    preferences.rewardAdActive = true
                                    requestNewRewardedInterstitial()
                                }
                            }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        _loadedRewardedInterstitial = null
                        preferences.rewardAdActive = false
                        infoLog(
                            "mRewardedInterstitialAd onAdFailedToLoad: ${loadAdError.message}",
                            "ForAstler: ADS"
                        )
                    }
                }
            )
        }

        if (_loadedInterstitial == null)
            InterstitialAd.load(
                context,
                _interstitialAdId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        _loadedInterstitial = interstitialAd
                        _loadedInterstitial?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    _loadedInterstitial = null
                                    requestNewInterstitial()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    super.onAdFailedToShowFullScreenContent(adError)
                                    _loadedInterstitial = null
                                    requestNewInterstitial()
                                }

                                override fun onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent()
                                    _loadedInterstitial = null
                                }
                            }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        _loadedInterstitial = null
                    }
                }
            )
    }

    private fun NativeAd.setupNativeBanner(pAdBindItem: ItemAdBinding) {
        pAdBindItem.adHeadline.text = headline
        pAdBindItem.adBody.text = body

        if (callToAction != null) {
            pAdBindItem.install.showView()
            pAdBindItem.install.text = callToAction
        } else {
            pAdBindItem.install.goneView()
        }

        if (icon == null) {
            pAdBindItem.adAppIcon.goneView()
            pAdBindItem.adAppIconCard.goneView()
        } else {
            val nDrawable = icon?.drawable

            nDrawable?.let {
                pAdBindItem.adAppIcon.setImageDrawable(it)
                pAdBindItem.adAppIcon.showView()
                pAdBindItem.adAppIconCard.showView()
            }
        }

        pAdBindItem.nativeAd.headlineView = pAdBindItem.adHeadline
        pAdBindItem.nativeAd.bodyView = pAdBindItem.adBody
        pAdBindItem.nativeAd.callToActionView = pAdBindItem.install

        pAdBindItem.nativeAd.setNativeAd(this)
    }

    private fun requestNewInterstitial() {
        if (_loadedInterstitial == null) {
            loadAd()
        }
    }

    private fun requestNewRewardedInterstitial() {
        if (_loadedRewardedInterstitial == null) {
            loadAd()
        }
    }

    @SuppressLint("VisibleForTests")
    private fun getTestDevicesList(): ArrayList<String> {
        val testDevices = arrayListOf(
            AdRequest.DEVICE_ID_EMULATOR
        )
        testDevices.addAll(gAppConfig.mTestDevices)

        return testDevices
    }
}