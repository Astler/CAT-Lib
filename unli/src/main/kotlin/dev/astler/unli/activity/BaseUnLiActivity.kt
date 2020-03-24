package dev.astler.unli.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import dev.astler.unli.AppSettings
import dev.astler.unli.PreferencesTool
import dev.astler.unli.appPrefs
import java.util.*

abstract class BaseUnLiActivity : AppCompatActivity() {

    var rewardedVideo: RewardedVideoAd? = null

    abstract fun preferencesTool(): PreferencesTool?

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppSettings.loadLocale(newBase, newBase.appPrefs.useEnglish))
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        rewardedVideo = MobileAds.getRewardedVideoAdInstance(this)

        rewardedVideo?.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoAdClosed() {}
            override fun onRewardedVideoAdLeftApplication() {}
            override fun onRewardedVideoAdLoaded() {}
            override fun onRewardedVideoAdOpened() {}
            override fun onRewardedVideoCompleted() {}

            override fun onRewarded(p0: RewardItem?) {
                preferencesTool()?.dayWithoutAds = GregorianCalendar.getInstance().get(GregorianCalendar.DATE)
                hideAd()
            }

            override fun onRewardedVideoStarted() {}
            override fun onRewardedVideoAdFailedToLoad(p0: Int) {}

        }
    }

    open fun hideAd() {

    }


    fun loadTheme(preferencesTool: PreferencesTool, @StyleRes lightThemeId: Int, @StyleRes darkThemeId: Int) {
        if (preferencesTool.isSystemTheme) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> setTheme(darkThemeId)
                Configuration.UI_MODE_NIGHT_NO -> setTheme(lightThemeId)
            }
        } else {
            if (preferencesTool.isDarkTheme) {
                setTheme(darkThemeId)
            } else {
                setTheme(lightThemeId)
            }
        }
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