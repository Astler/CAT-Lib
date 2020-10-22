package dev.astler.unli.activity

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.navigation.NavigationView
import dev.astler.unli.*
import dev.astler.unli.R
import dev.astler.unli.interfaces.ActivityInterface
import dev.astler.unli.utils.*
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseUnLiActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener, ActivityInterface {

    lateinit var mRewardedVideo: RewardedAd
    private lateinit var mInterstitialAd: InterstitialAd
    lateinit var mPreferencesTool: PreferencesTool
    private var showAdAfterLoading = false
    var infoDialog: AlertDialog? = null
    var mActiveFragment: Fragment? = null

    open fun initPreferencesTool(): PreferencesTool = PreferencesTool(this)

    open var mInterstitialAdId = ""
    open var mRewardedAdId = ""
    open var mAppMenuId = 0

    open fun updateNavigationMenu() {}

    override fun setCurrentFragment(fragment: Fragment) {
        mActiveFragment = fragment
    }

    open fun getTestDevicesList(): ArrayList<String> {
        return arrayListOf(AdRequest.DEVICE_ID_EMULATOR, "46BCDEE9C1F5ED2ADF3A5DB3889DDFB5")
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppSettings.loadLocale(newBase))
    }

    open fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    open var setTagForChildDirectedTreatment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UnliApp.getInstance().initAppLanguage(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        val testDevices = ArrayList<String>()
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR)
        testDevices.addAll(getTestDevicesList())

        val requestConfiguration = RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)

        if (setTagForChildDirectedTreatment) {
            requestConfiguration.setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
        }

        MobileAds.setRequestConfiguration(requestConfiguration.build())

        setDefaultPreferences()

        mPreferencesTool = initPreferencesTool()
        loadTheme(R.style.AppUnliTheme, R.style.AppUnliDarkTheme)

        mRewardedVideo = RewardedAd(this, mRewardedAdId)

        if (canShowAds() && mRewardedAdId.isNotEmpty()) {
            mRewardedVideo.loadAd(AdRequest.Builder().build(), onRewardedAdLoadCallback())
        }

        mInterstitialAd = InterstitialAd(this)

        if (mInterstitialAdId.isNotEmpty()) {

            mInterstitialAd.adUnitId = mInterstitialAdId
            mInterstitialAd.loadAd(getAdRequest())

            mInterstitialAd.adListener = object : AdListener() {
                override fun onAdClosed() {
                    mInterstitialAd.loadAd(getAdRequest())
                }
            }
        }
    }

    open fun onRewardedAdLoadCallback(): RewardedAdLoadCallback = object : RewardedAdLoadCallback() {
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
            val preferencesTool = PreferencesTool(this@BaseUnLiActivity)
            preferencesTool.dayWithoutAds =
                    GregorianCalendar.getInstance().get(GregorianCalendar.DATE)
        }

        override fun onRewardedAdFailedToShow(adError: AdError) {}
    }

    override fun showInterstitialAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            if (!mInterstitialAd.isLoading)
                mInterstitialAd.loadAd(getAdRequest())
        }
    }

    override fun showRewardAd() {
        if (mRewardedVideo.isLoaded) {
            mRewardedVideo.show(this, onRewardedAdCallback())
        } else {
            infoDialog = showInfoDialog(R.string.just_a_minute, R.string.ad_is_loading)
            infoDialog?.show()

            showAdAfterLoading = true
        }
    }

    open fun hideAd() {}

    open fun setDefaultPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)
    }

    fun loadTheme(@StyleRes lightThemeId: Int, @StyleRes darkThemeId: Int) {
        if (mPreferencesTool.isSystemTheme) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> setTheme(darkThemeId)
                Configuration.UI_MODE_NIGHT_NO -> setTheme(lightThemeId)
            }
        } else {
            if (mPreferencesTool.isDarkTheme) {
                setTheme(darkThemeId)
            } else {
                setTheme(lightThemeId)
            }
        }
    }

    open fun navigationViewInflateMenus(navigationView: NavigationView) {
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
            R.id.rate_app -> this.rateApp()
            R.id.more_apps -> this.moreApps()
            R.id.settings -> navToSettingsFragment()
            R.id.about -> navToAboutFragment()
        }

        return true
    }

    private fun showNoAdsDialog() {
        AlertDialog.Builder(this).setMessage(R.string.disable_ads_msg).setTitle(R.string.disable_ads)
                .setPositiveButton(R.string.buy_pro) { _, _ ->
                    openMarketApp("com.astler.knowlegebook_paid")
                }.setNeutralButton(R.string.watch_ads) { _, _ ->
                    showRewardAd()
                }.create().show()
    }

    open fun navToSettingsFragment() {}
    open fun navToAboutFragment() {}

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if (key == PreferencesTool.appThemeKey) {
            recreate()
        }

        if (key == PreferencesTool.appLocaleKey) {
            recreate()
        }

        if (key == PreferencesTool.dayWithoutAdsKey) {
            hideAd()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val preferencesTool = PreferencesTool(this)

        if (preferencesTool.isSystemTheme) {
            when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    recreate()
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    recreate()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mPreferencesTool.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        mPreferencesTool.unregisterListener(this)
    }
}