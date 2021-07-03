package dev.astler.billing

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.* // ktlint-disable no-wildcard-imports
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dev.astler.unlib.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.ui.R
import dev.astler.unlib.utils.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib_ads.utils.canShowAds
import kotlinx.coroutines.launch
import java.util.* // ktlint-disable no-wildcard-imports
import kotlin.collections.ArrayList
import kotlin.random.Random

abstract class UnLibAdsBillingActivity : UnLibBillingActivity(), OnUserEarnedRewardListener {

    protected lateinit var mRemoteConfig: FirebaseRemoteConfig
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

        lifecycleScope.launch {
            val nTimeFromStart = LocalStorage.readDirectValue(
                PreferencesKeys.APP_START_TIME_KEY,
                GregorianCalendar().timeInMillis
            )
            val nLastAdTime = LocalStorage.readDirectValue(PreferencesKeys.LAST_AD_TIME_KEY, 0)

            val nShowAds = mRemoteConfig.getBoolean("show_interstitial_ad_$mConfigAppPackage")
            val nAdsPause = mRemoteConfig.getLong("ad_pause_$mConfigAppPackage").toInt()
            val nAdsChance = mRemoteConfig.getLong("ad_chance_$mConfigAppPackage").toInt()

            adLog("UNLIB_AD: loaded ads config = $nShowAds, $nAdsPause, $nAdsChance")

            if (nShowAds &&
                canShowAds() &&
                nTimeFromStart >= 10000 &&
                nLastAdTime >= nAdsPause
            ) {
                val randNum = Random.nextInt(nAdsChance)

                adLog("UNLIB_AD: AD CHANCE: $randNum/$nAdsChance")

                if (randNum == 0)
                    showInterstitialAd()
            } else {
                adLog("UNLIB_AD: Dont show ad! Possible reasons?\ncanShowAds = ${canShowAds()}, enabled in config? -> $nShowAds, time from start out? -> ${nTimeFromStart >= 10000}, time from last ad? -> ${nLastAdTime >= nAdsPause}")
            }
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

    open var setTagForChildDirectedTreatment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            setTagForChildDirectedTreatment = LocalStorage.readDirectValue(PreferencesKeys.CHILD_ADS, mNeedAgeCheck)
        }

        UnliApp.getInstance().initAppLanguage(this)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        mRemoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }

        mRemoteConfig.setConfigSettingsAsync(configSettings)

        mRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    infoLog("Config params updated: $updated")
                }
            }

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
        lifecycleScope.launch {
            if (!LocalStorage.readDirectValue(PreferencesKeys.AGE_CONFIRMED_ADS, false) && mNeedAgeCheck) {
                confirmDialog(
                    getString(dev.astler.unlib_ads.R.string.ads_dialog_title),
                    getString(dev.astler.unlib_ads.R.string.ads_dialog_msg),
                    getString(R.string.yes), getString(R.string.no),
                    pPositiveAction = {
                        lifecycleScope.launch {
                            LocalStorage.storeValue(PreferencesKeys.CHILD_ADS, false)
                        }
                    },
                    pNegativeAction = {
                        lifecycleScope.launch {
                            LocalStorage.storeValue(PreferencesKeys.CHILD_ADS, true)
                        }
                    }
                )

                LocalStorage.storeValue(PreferencesKeys.AGE_CONFIRMED_ADS, true)
                setTagForChildDirectedTreatment = LocalStorage.readDirectValue(PreferencesKeys.CHILD_ADS, mNeedAgeCheck)
            }
        }
        MobileAds.setRequestConfiguration(requestConfiguration.build())

        loadAd()

        lifecycleScope.launch {
            LocalStorage.noAdsDayWatcher {
                if (!canShowAds())
                    hideAd()
            }
        }
    }

    override fun onUserEarnedReward(p0: RewardItem) {
        lifecycleScope.launch {
            LocalStorage.setNoDayAds()
        }
    }

    override fun showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
            lifecycleScope.launch {
                LocalStorage.setLastAdTime(GregorianCalendar().timeInMillis)
            }
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
                                    this@UnLibAdsBillingActivity.mRewardedInterstitialAd = null
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
                                    this@UnLibAdsBillingActivity.mInterstitialAd = null
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
