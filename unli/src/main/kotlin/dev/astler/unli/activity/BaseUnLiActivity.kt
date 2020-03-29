package dev.astler.unli.activity

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.material.navigation.NavigationView
import dev.astler.unli.*
import dev.astler.unli.R
import dev.astler.unli.interfaces.ActivityInterface
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseUnLiActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener, ActivityInterface {

    var rewardedVideo: RewardedVideoAd? = null
    private lateinit var mInterstitialAd: InterstitialAd
    lateinit var mPreferencesTool: PreferencesTool

    abstract fun initPreferencesTool(): PreferencesTool

    open fun getInterstitialAdId() = ""

    open fun getTestDevicesList(): ArrayList<String> {
        return arrayListOf("46BCDEE9C1F5ED2ADF3A5DB3889DDFB5")
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppSettings.loadLocale(newBase, newBase.appPrefs.useEnglish))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val testDevices = ArrayList<String>()
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR)
        testDevices.addAll(getTestDevicesList())

        val requestConfiguration = RequestConfiguration.Builder()
            .setTestDeviceIds(testDevices)
            .build()

        MobileAds.setRequestConfiguration(requestConfiguration)

        setDefaultPreferences()

        mPreferencesTool = initPreferencesTool()

        rewardedVideo = MobileAds.getRewardedVideoAdInstance(this)

        rewardedVideo?.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoAdClosed() {}
            override fun onRewardedVideoAdLeftApplication() {}
            override fun onRewardedVideoAdLoaded() {}
            override fun onRewardedVideoAdOpened() {}
            override fun onRewardedVideoCompleted() {}

            override fun onRewarded(p0: RewardItem?) {
                val preferencesTool = PreferencesTool(this@BaseUnLiActivity)

                preferencesTool.dayWithoutAds = GregorianCalendar.getInstance().get(GregorianCalendar.DATE)
                hideAd()
            }

            override fun onRewardedVideoStarted() {}
            override fun onRewardedVideoAdFailedToLoad(p0: Int) {}

        }

        mInterstitialAd = InterstitialAd(this)

        if (getInterstitialAdId().isNotEmpty()) {

            mInterstitialAd.adUnitId = getInterstitialAdId()
            mInterstitialAd.loadAd(AdRequest.Builder().build())

            mInterstitialAd.adListener = object : AdListener() {
                override fun onAdClosed() {
                    mInterstitialAd.loadAd(AdRequest.Builder().build())
                }
            }
        }
    }


    override fun showInterstitialAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        }
        else {
            if (!mInterstitialAd.isLoading)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
        }
    }

    open fun hideAd() {

    }

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
        navigationView.inflateMenu(R.menu.base_activity_drawer)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.rate_app -> this.rateApp()
            R.id.more_apps -> this.moreApps()
            R.id.settings -> navToSettingsFragment()
        }

        return true
    }

    abstract fun navToSettingsFragment()

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PreferencesTool.appThemeKey || key == PreferencesTool.appLocaleModeKey || key == PreferencesTool.appLocaleKey) {
            recreate()
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


//    fun updateBackgroundColor(color: Int = appPrefs.backgroundColor) {
//        window.decorView.setBackgroundColor(color)
//    }
//
//    fun updateActionbarColor(color: Int = appPrefs.primaryColor) {
//        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
//        updateActionBarTitle(supportActionBar?.title.toString(), color)
//        updateStatusbarColor(color)
//        //setTaskDescription(ActivityManager.TaskDescription(null, null, color))
//    }
//
//    fun updateStatusbarColor(color: Int) {
//        window.statusBarColor = color.darkenColor()
//    }

}