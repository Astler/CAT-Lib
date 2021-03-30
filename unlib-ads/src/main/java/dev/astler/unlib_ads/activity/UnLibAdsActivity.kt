package dev.astler.unlib_ads.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.navigation.NavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dev.astler.unlib.PreferencesTool
import dev.astler.unlib.UnliApp
import dev.astler.unlib.gAppConfig
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.ui.R
import dev.astler.unlib.ui.activity.BaseUnLiActivity
import dev.astler.unlib.ui.interfaces.ActivityInterface
import dev.astler.unlib.utils.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

abstract class UnLibAdsActivity : BaseUnLiActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    SharedPreferences.OnSharedPreferenceChangeListener, ActivityInterface {

    protected lateinit var mRemoteConfig: FirebaseRemoteConfig
    protected var mAdView: AdView? = null

    lateinit var mRewardedVideo: RewardedAd
    private var mInterstitialAd: InterstitialAd? = null
    private var showAdAfterLoading = false
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

        if (nShowAds
            && canShowAds()
            && nTimeFromStart >= 10000
            && nTimeFromLastAd >= nAdsPause
        ) {
            val randNum = Random.nextInt(nAdsChance)

            infoLog("UNLIB_AD: AD CHANCE: ${randNum}/${nAdsChance}")

            if (randNum == 0)
                showInterstitialAd()
        } else {
            infoLog("UNLIB_AD: Dont show ad! Possible reasons?\ncanShowAds = ${canShowAds()}, enabled in config? -> $nShowAds, time from start out? -> ${nTimeFromStart >= 10000}, time from last ad? -> ${nTimeFromLastAd >= nAdsPause}")
        }
    }

    open fun getTestDevicesList(): ArrayList<String> {
        return arrayListOf(AdRequest.DEVICE_ID_EMULATOR, "46BCDEE9C1F5ED2ADF3A5DB3889DDFB5", gAppConfig.mLastTestDevice)
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

        if (!gPreferencesTool.getBoolean("age_confirmed", false) && mNeedAgeCheck) {
            unLibDialog(getString(dev.astler.unlib_ads.R.string.ads_dialog_title),
                getString(dev.astler.unlib_ads.R.string.ads_dialog_msg),
                getString(R.string.yes), getString(R.string.no),
                pPositiveClick = {
                    gPreferencesTool.edit("child_ads", false)
                },
                pNegativeClick = {
                    gPreferencesTool.edit("child_ads", true)
                }
            )

            gPreferencesTool.edit("age_confirmed", true)
        }

        MobileAds.setRequestConfiguration(requestConfiguration.build())

        setDefaultPreferences()

        mRewardedVideo = RewardedAd(this, mRewardedAdId)

        if (canShowAds() && mRewardedAdId.isNotEmpty()) {
            mRewardedVideo.loadAd(AdRequest.Builder().build(), onRewardedAdLoadCallback())
        }

        loadAd()
    }

    open fun onRewardedAdLoadCallback(): RewardedAdLoadCallback =
        object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                if (showAdAfterLoading)
                    showRewardAd()

                infoDialog?.dismiss()
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {

            }
        }

    open fun onRewardedAdCallback(): RewardedAdCallback = object : RewardedAdCallback() {
        override fun onRewardedAdOpened() {}

        override fun onRewardedAdClosed() {
            mRewardedVideo.loadAd(AdRequest.Builder().build(), onRewardedAdLoadCallback())
        }

        override fun onUserEarnedReward(@NonNull reward: RewardItem) {
            val preferencesTool = PreferencesTool(this@UnLibAdsActivity)
            preferencesTool.dayWithoutAds =
                GregorianCalendar.getInstance().get(GregorianCalendar.DATE)
        }

        override fun onRewardedAdFailedToShow(adError: AdError) {}
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
        if (mRewardedVideo.isLoaded) {
            mRewardedVideo.show(this, onRewardedAdCallback())
        } else {
            //  infoDialog = showInfoDialog(R.string.just_a_minute, R.string.ad_is_loading)
            infoDialog?.show()

            showAdAfterLoading = true
        }
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
            unLibDialog(
                getString(R.string.disable_ads),
                getString(R.string.disable_ads_msg),
                getString(R.string.buy_pro), getString(R.string.watch_ads), pPositiveClick = {
                    openMarketApp(mProPackageName)
                }, pNegativeClick = { showRewardAd() })
        } else {
            unLibDialog(
                getString(R.string.disable_ads),
                getString(R.string.disable_ads_msg),
                getString(R.string.watch_ads), pPositiveClick = {
                    showRewardAd()
                })
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        if (key == PreferencesTool.dayWithoutAdsKey) {
            hideAd()
        }
    }

    private fun loadAd() {
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
            })
    }

    private fun requestNewInterstitial() {
        if (mInterstitialAd == null) {
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