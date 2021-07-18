package dev.astler.unlib_ads.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.* // ktlint-disable no-wildcard-imports
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import dev.astler.unlib.PreferencesTool
import dev.astler.unlib.UnliApp
import dev.astler.unlib.gAppConfig
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.ui.R
import dev.astler.unlib.ui.activity.BaseUnLiActivity
import dev.astler.unlib.utils.* // ktlint-disable no-wildcard-imports
import java.util.* // ktlint-disable no-wildcard-imports
import kotlin.collections.ArrayList
import kotlin.random.Random

abstract class UnLibAdsActivity : BaseUnLiActivity(), OnUserEarnedRewardListener {

    protected var mAdView: AdView? = null

    private var mRewardedInterstitialAd: RewardedInterstitialAd? = null
    private var mInterstitialAd: InterstitialAd? = null

    var infoDialog: AlertDialog? = null

    open var mInterstitialAdId = gAppConfig.mInterstitialAdId
    open var mRewardedAdId = gAppConfig.mRewardedAdId
    open var mProPackageName = gAppConfig.mProPackageName

    open val mConfigAppPackage: String by lazy {
        packageName.replace(".", "_")
    }

    override fun setCurrentFragment(fragment: Fragment) {
        super.setCurrentFragment(fragment)

        val nTimeFromStart =
            GregorianCalendar().timeInMillis - gPreferencesTool.getLong(
                "start_time",
                GregorianCalendar().timeInMillis
            )

        val nTimeFromLastAd =
            GregorianCalendar().timeInMillis - gPreferencesTool.getLong(
                "last_ad_show", 0
            )

        val nShowAds = mRemoteConfig.getBoolean("show_interstitial_ad_$mConfigAppPackage")
        val nAdsPause = mRemoteConfig.getLong("ad_pause_$mConfigAppPackage").toInt()
        val nAdsChance = mRemoteConfig.getLong("ad_chance_$mConfigAppPackage").toInt()

        infoLog("UNLIB_AD: loaded ads config = $nShowAds, $nAdsPause, $nAdsChance")

        if (nShowAds &&
            canShowAds() &&
            nTimeFromStart >= 10000 &&
            nTimeFromLastAd >= nAdsPause
        ) {
            val randNum = Random.nextInt(nAdsChance)

            infoLog("UNLIB_AD: AD CHANCE: $randNum/$nAdsChance")

            if (randNum == 0)
                showInterstitialAd()
        } else {
            infoLog("UNLIB_AD: Dont show ad! Possible reasons?\ncanShowAds = ${canShowAds()}, enabled in config? -> $nShowAds, time from start out? -> ${nTimeFromStart >= 10000}, time from last ad? -> ${nTimeFromLastAd >= nAdsPause}")
        }
    }

    open fun getTestDevicesList(): ArrayList<String> {
        val nTestDevices = arrayListOf(
            AdRequest.DEVICE_ID_EMULATOR
        )
        nTestDevices.addAll(gAppConfig.mTestDevices)

        return nTestDevices
    }

    open fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    open var mNeedAgeCheck: Boolean = gAppConfig.mNeedAgeCheck

    open val setTagForChildDirectedTreatment: Boolean by lazy {
        gPreferencesTool.getBoolean("child_ads", mNeedAgeCheck)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UnliApp.getInstance().initAppLanguage(this)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        gPreferencesTool.edit("start_time", GregorianCalendar().timeInMillis)

        val testDevices = ArrayList<String>()
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR)
        testDevices.addAll(getTestDevicesList())

        val requestConfiguration = RequestConfiguration.Builder()
            .setTestDeviceIds(testDevices)

        if (setTagForChildDirectedTreatment) {
            requestConfiguration.setTagForChildDirectedTreatment(
                TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
            )
        }

        if (!gPreferencesTool.getBoolean("age_confirmed", false) && mNeedAgeCheck) {
            confirmDialog(
                getString(dev.astler.unlib_ads.R.string.ads_dialog_title),
                getString(dev.astler.unlib_ads.R.string.ads_dialog_msg),
                getString(R.string.yes), getString(R.string.no),
                pPositiveAction = {
                    gPreferencesTool.edit("child_ads", false)
                },
                pNegativeAction = {
                    gPreferencesTool.edit("child_ads", true)
                }
            )

            gPreferencesTool.edit("age_confirmed", true)
        }

        MobileAds.setRequestConfiguration(requestConfiguration.build())

        setDefaultPreferences()

        loadAd()
    }

    override fun onUserEarnedReward(p0: RewardItem) {
        val preferencesTool = PreferencesTool(this@UnLibAdsActivity)
        preferencesTool.dayWithoutAds =
            GregorianCalendar.getInstance().get(GregorianCalendar.DATE)
    }

    override fun showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
            gPreferencesTool.edit("last_ad_show", GregorianCalendar().timeInMillis)
        } else {
            requestNewInterstitial()
        }
    }

    override fun showRewardAd() {
        mRewardedInterstitialAd?.show(this, this)
    }

    open fun hideAd() {
        mAdView?.goneView()
    }

    override fun navigationViewInflateMenus(navigationView: NavigationView) {
        navigationView.menu.clear()

        if (canShowAds())
            navigationView.inflateMenu(R.menu.ad_menu)

        if (mAppMenuId != 0)
            navigationView.inflateMenu(mAppMenuId)

        navigationView.inflateMenu(R.menu.base_activity_drawer)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.adItem -> {
                showNoAdsDialog()
            }
        }

        return super.onNavigationItemSelected(item)
    }

    private fun showNoAdsDialog() {
        if (mProPackageName.isNotEmpty()) {
            confirmDialog(
                getString(R.string.disable_ads),
                getString(R.string.disable_ads_msg),
                getString(R.string.buy_pro), getString(R.string.watch_ads),
                pPositiveAction = {
                    openAppInPlayStore(mProPackageName)
                },
                pNegativeAction = { showRewardAd() }
            )
        } else {
            confirmDialog(
                getString(R.string.disable_ads),
                getString(R.string.disable_ads_msg),
                getString(R.string.watch_ads),
                pPositiveAction = {
                    showRewardAd()
                }
            )
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        if (key == PreferencesTool.dayWithoutAdsKey) {
            hideAd()
        }
    }

    private fun loadAd() {
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
                                    requestNewInterstitial()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    super.onAdFailedToShowFullScreenContent(adError)
                                    mRewardedInterstitialAd = null
                                    requestNewRewardedInterstitial()
                                }

                                override fun onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent()
                                    this@UnLibAdsActivity.mRewardedInterstitialAd = null
                                    requestNewRewardedInterstitial()
                                }
                            }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        mRewardedInterstitialAd = null
                        infoLog("mRewardedInterstitialAd onAdFailedToLoad", "ForAstler: ADS")
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
                                    this@UnLibAdsActivity.mInterstitialAd = null
                                }
                            }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        mInterstitialAd = null
                    }
                }
            )
    }

    private fun requestNewInterstitial() {
        if (mInterstitialAd == null) {
            loadAd()
        }
    }

    private fun requestNewRewardedInterstitial() {
        if (mRewardedInterstitialAd == null) {
            loadAd()
        }
    }

    override fun onPause() {
        super.onPause()
        mAdView?.pause()
    }

    override fun onResume() {
        super.onResume()
        mAdView?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdView?.destroy()
    }
}
