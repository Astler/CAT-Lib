package dev.astler.unlib_ads.utils

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.* // ktlint-disable no-wildcard-imports
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.ResumeTimeKey
import dev.astler.cat_ui.StartTimeKey
import dev.astler.cat_ui.utils.dialogs.confirmDialog
import dev.astler.cat_ui.utils.views.goneView
import dev.astler.cat_ui.utils.views.showView
import dev.astler.unlib.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.utils.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib_ads.R
import dev.astler.unlib_ads.databinding.ItemAdBinding
import dev.astler.unlib_ads.interfaces.HideAdListener
import dev.astler.unlib_ads.utils.dialogs.adsAgeDialog
import java.util.* // ktlint-disable no-wildcard-imports
import kotlin.random.Random

/**
 * How to use Ads Acitivty Module
 *
 * 1. Initiliaze it in onCreate with initAds
 * 2 (optional). Add Ad Listeners in SharedPreferences Listener -> adPreferencesListener(key)
 * 3 (optional). To show interstitial ad with fragment change add interstitialAdsShowTry() to setCurrentFragment
 * 4 (optional). If Activity with NavigationView should contain no ads option: add
 *  cNoAdsMenuItemId -> {
 *       showNoAdsDialog()
 *   }
 *   to click listener and navigationView.addNoAdsItem() to menu init
 */

private var mRewardedInterstitialAd: RewardedInterstitialAd? = null
private var mInterstitialAd: InterstitialAd? = null

var mNeedAgeCheck: Boolean = gAppConfig.mNeedAgeCheck

val setTagForChildDirectedTreatment: Boolean by lazy {
    gPreferencesTool.getBoolean("child_ads", mNeedAgeCheck)
}

var mInterstitialAdId = gAppConfig.mInterstitialAdId
var mRewardedAdId = gAppConfig.mRewardedAdId
var mProPackageName = gAppConfig.mProPackageName

@SuppressLint("VisibleForTests")
private fun getTestDevicesList(): ArrayList<String> {
    val nTestDevices = arrayListOf(
        AdRequest.DEVICE_ID_EMULATOR
    )
    nTestDevices.addAll(gAppConfig.mTestDevices)

    return nTestDevices
}

fun AppCompatActivity.initAds() {
    val requestConfiguration = RequestConfiguration.Builder()
        .setTestDeviceIds(getTestDevicesList())

    if (setTagForChildDirectedTreatment) {
        requestConfiguration.setTagForChildDirectedTreatment(
            RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
        )
    }

    if (!gPreferencesTool.getBoolean("age_confirmed", false) && mNeedAgeCheck) {
        adsAgeDialog()
        gPreferencesTool.edit("age_confirmed", true)
    }

    MobileAds.setRequestConfiguration(requestConfiguration.build())

    loadAd()
}

val mNoAdsRewardListener: OnUserEarnedRewardListener
    get() = OnUserEarnedRewardListener {
        gPreferencesTool.noAdsDay = GregorianCalendar.getInstance().get(GregorianCalendar.DATE)
        gPreferencesTool.unsetRewardAdIsActive()
    }

fun AppCompatActivity.showInterstitialAd() {
    if (mInterstitialAd != null) {
        mInterstitialAd?.show(this)
        gPreferencesTool.edit("last_ad_show", GregorianCalendar().timeInMillis)
    } else {
        requestNewInterstitial()
    }
}

fun getAdRequest(): AdRequest {
    return AdRequest.Builder().build()
}

fun AppCompatActivity.showRewardAd() {
    mRewardedInterstitialAd?.show(this, mNoAdsRewardListener)
    gPreferencesTool.setRewardAdIsActive()
}

fun AppCompatActivity.requestNewInterstitial() {
    if (mInterstitialAd == null) {
        loadAd()
    }
}

fun AppCompatActivity.requestNewRewardedInterstitial() {
    if (mRewardedInterstitialAd == null) {
        loadAd()
    }
}

private fun AppCompatActivity.loadAd() {
    if (canShowAds() && mRewardedAdId.isNotEmpty() && mRewardedInterstitialAd == null) {
        RewardedInterstitialAd.load(
            this,
            mRewardedAdId,
            getAdRequest(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    mRewardedInterstitialAd = ad
                    infoLog("mRewardedInterstitialAd onAdLoaded", "ForAstler: ADS")
                    mRewardedInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                mRewardedInterstitialAd = null
                                gPreferencesTool.unsetRewardAdIsActive()
                                requestNewInterstitial()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                mRewardedInterstitialAd = null
                                gPreferencesTool.unsetRewardAdIsActive()
                                requestNewRewardedInterstitial()
                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                mRewardedInterstitialAd = null
                                gPreferencesTool.setRewardAdIsActive()
                                requestNewRewardedInterstitial()
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    mRewardedInterstitialAd = null
                    gPreferencesTool.unsetRewardAdIsActive()
                    infoLog("mRewardedInterstitialAd onAdFailedToLoad: ${loadAdError.message}", "ForAstler: ADS")
                }
            }
        )
    }

    if (mInterstitialAd == null)
        InterstitialAd.load(
            this,
            mInterstitialAdId,
            getAdRequest(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                mInterstitialAd = null
                                requestNewInterstitial()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                mInterstitialAd = null
                                requestNewInterstitial()
                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                mInterstitialAd = null
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    mInterstitialAd = null
                }
            }
        )
}



fun AppCompatActivity.adPreferencesListener(pKey: String?) {
    if (pKey == PreferencesTool.dayWithoutAdsKey) {
        if (this is HideAdListener)
            hideAd()
    }
}

fun NavigationView.addNoAdsItem() {
    if (context.canShowAds())
        inflateMenu(R.menu.ad_menu)
}

fun CatActivity.interstitialAdsShowTry() {
    if (!canShowAds()) {
        adsLog("Can't show ads!")
        return
    }

    val nShowAds = mRemoteConfig.getBoolean("show_interstitial_ad_$configName")

    if (!nShowAds) {
        adsLog("Ads disabled in remote config!")
        return
    }

    val nAdsPause = mRemoteConfig.getLong("ad_pause_$configName").toInt()
    val nAdsChance = mRemoteConfig.getLong("ad_chance_$configName").toInt()

    adsLog("Loaded ads remote config = $nAdsPause, $nAdsChance")

    val nIsTimeFromStartPassed = StartTimeKey.hasPrefsTimePassed(10000)

    if (!nIsTimeFromStartPassed) {
        adsLog("nIsTimeFromStartPassed not passed! = $nIsTimeFromStartPassed")
        return
    }

    val nIsTimeFromResumePassed = ResumeTimeKey.hasPrefsTimePassed(5000)

    if (!nIsTimeFromResumePassed) {
        adsLog("nIsTimeFromResumePassed not passed! = $nIsTimeFromResumePassed")
        return
    }

    val nIsAdsPausePassed = "last_ad_show".hasPrefsTimePassed(nAdsPause.toLong(), 0L)

    if (!nIsAdsPausePassed) {
        adsLog("nIsAdsPausePassed not passed! $nIsAdsPausePassed")
        return
    }

    val randNum = Random.nextInt(nAdsChance)

    adsLog("AD CHANCE: $randNum/$nAdsChance")

    if (randNum == 0)
        showInterstitialAd()
}

fun AppCompatActivity.createNativeAdLoader(pAdBindItem: ItemAdBinding): AdLoader {
    var nAdLoader: AdLoader? = null

    nAdLoader = AdLoader.Builder(this, gAppConfig.mNativeAdId)
        .forNativeAd { nativeAd: NativeAd ->
            if (nAdLoader?.isLoading == true) {
                adsLog("Native Ad Banner is loading")
            } else {
                adsLog("Native Ad Banner is loaded")
                nativeAd.setupNativeBanner(pAdBindItem)
            }

            if (isDestroyed) {
                nativeAd.destroy()
                return@forNativeAd
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

fun NativeAd.setupNativeBanner(pAdBindItem: ItemAdBinding) {
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
