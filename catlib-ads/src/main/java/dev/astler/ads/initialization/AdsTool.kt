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
import dev.astler.catlib.ads.databinding.ItemAdBinding
import dev.astler.catlib.gAppConfig
import dev.astler.catlib.gPreferencesTool
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.RemoteConfigProvider
import dev.astler.catlib.utils.adsLog
import dev.astler.catlib.utils.canShowAds
import dev.astler.catlib.utils.formattedPackageName
import dev.astler.catlib.utils.hasPrefsTimePassed
import dev.astler.catlib.utils.infoLog
import java.util.GregorianCalendar
import javax.inject.Inject

class AdsTool @Inject constructor(
    val context: Context,
    preferences: PreferencesTool,
    val remoteConfig: RemoteConfigProvider
) :
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var _remoteAdsConfig: RemoteConfigData? = null
    private var _configPackageName: String = context.formattedPackageName()
    private var _needAgeCheck: Boolean = gAppConfig.mNeedAgeCheck

    private var _interstitialAdId = gAppConfig.mInterstitialAdId
    private var _rewardedAdId = gAppConfig.mRewardedAdId

    private var _rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var _interstitialAd: InterstitialAd? = null

    init {
        if (context is AppCompatActivity) {
            context.lifecycleScope.launchWhenResumed {
                initializeAds()
            }

            gPreferencesTool.addListener(this)

            fetchRemoteConfigForAds()
        }
    }

    private val _noAdsRewardListener: OnUserEarnedRewardListener
        get() = OnUserEarnedRewardListener {
            gPreferencesTool.noAdsDay = GregorianCalendar.getInstance().get(GregorianCalendar.DATE)
            gPreferencesTool.rewardAdActive = false
        }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PreferencesTool.dayWithoutAdsKey) {
            if (context is IAdListener) {
                context.hideAd()
            }
        }
    }

    private fun fetchRemoteConfigForAds() {
        remoteConfig.loadRemoteData {
            _remoteAdsConfig = RemoteConfigData(
                remoteConfig.getLong(startAdDelayKey + context.formattedPackageName()),
                remoteConfig.getLong(resumeAdDelayKey + context.formattedPackageName()),
                remoteConfig.getLong(lastAdDelayKey + context.formattedPackageName()),
                remoteConfig.getLong(adChanceKey + context.formattedPackageName()),
                remoteConfig.getBoolean(interstitialAdEnabledKey + context.formattedPackageName()),
                remoteConfig.getBoolean(openAdEnabledKey + context.formattedPackageName()),
                remoteConfig.getBoolean(bannerAdEnabledKey + context.formattedPackageName()),
                remoteConfig.getBoolean(rewardAdEnabledKey + context.formattedPackageName()),
            )

            adsLog("loaded ads config $_remoteAdsConfig")
        }
    }

    fun showRewardAd() {
        if (context is AppCompatActivity) {
            if (_rewardedInterstitialAd != null) {
                _rewardedInterstitialAd?.show(context, _noAdsRewardListener)
                gPreferencesTool.rewardAdActive = true
            } else {
                requestNewRewardedInterstitial();
            }
        }
    }

    fun showInterstitialAd() {
        if (context is AppCompatActivity) {
            if (_interstitialAd != null) {
                _interstitialAd?.show(context)
                gPreferencesTool.lastAdsTime = GregorianCalendar().timeInMillis
            } else {
                requestNewInterstitial()
            }
        } else {
            adsLog("Cant show ads, context is not AppCompatActivity")
        }
    }

    fun interstitialAdsShowTry() {
        if (!context.canShowAds()) {
            adsLog("Can't show ads!")
            return
        }

        if (_remoteAdsConfig == null) {
            fetchRemoteConfigForAds()
            return
        }

        val config = _remoteAdsConfig!!

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

        if (!gPreferencesTool.lastAdsTime.hasPrefsTimePassed(fromLastDelay * 1000L)) {
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

            if (gPreferencesTool.childAdsMode) {
                requestConfiguration.setTagForChildDirectedTreatment(
                    RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
                )
            }

            MobileAds.setRequestConfiguration(requestConfiguration.build())

            loadAd()
        }

        if (!gPreferencesTool.ageConfirmed && _needAgeCheck) {
            context.adsAgeConfirmDialog {
                gPreferencesTool.childAdsMode = false
                setupMobileAds()
            }

            gPreferencesTool.ageConfirmed = true
        } else {
            setupMobileAds()
        }
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        if (context.canShowAds() && _rewardedAdId.isNotEmpty() && _rewardedInterstitialAd == null) {
            RewardedInterstitialAd.load(
                context,
                _rewardedAdId,
                adRequest,
                object : RewardedInterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedInterstitialAd) {
                        _rewardedInterstitialAd = ad
                        infoLog("mRewardedInterstitialAd onAdLoaded", "ForAstler: ADS")
                        _rewardedInterstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    _rewardedInterstitialAd = null
                                    gPreferencesTool.rewardAdActive = false
                                    requestNewInterstitial()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    super.onAdFailedToShowFullScreenContent(adError)
                                    _rewardedInterstitialAd = null
                                    gPreferencesTool.rewardAdActive = false
                                    requestNewRewardedInterstitial()
                                }

                                override fun onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent()
                                    _rewardedInterstitialAd = null
                                    gPreferencesTool.rewardAdActive = true
                                    requestNewRewardedInterstitial()
                                }
                            }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        _rewardedInterstitialAd = null
                        gPreferencesTool.rewardAdActive = false
                        infoLog(
                            "mRewardedInterstitialAd onAdFailedToLoad: ${loadAdError.message}",
                            "ForAstler: ADS"
                        )
                    }
                }
            )
        }

        if (_interstitialAd == null)
            InterstitialAd.load(
                context,
                _interstitialAdId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        _interstitialAd = interstitialAd
                        _interstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    _interstitialAd = null
                                    requestNewInterstitial()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    super.onAdFailedToShowFullScreenContent(adError)
                                    _interstitialAd = null
                                    requestNewInterstitial()
                                }

                                override fun onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent()
                                    _interstitialAd = null
                                }
                            }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        _interstitialAd = null
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
        if (_interstitialAd == null) {
            loadAd()
        }
    }

    private fun requestNewRewardedInterstitial() {
        if (_rewardedInterstitialAd == null) {
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

    fun startNativeAdsLoader() {
        NativeAdsLoader.instance?.loadAds(context, AdRequest.Builder().build())
    }
}