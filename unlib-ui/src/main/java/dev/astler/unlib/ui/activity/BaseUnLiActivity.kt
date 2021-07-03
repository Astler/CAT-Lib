package dev.astler.unlib.ui.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dev.astler.unlib.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.interfaces.ActivityInterface
import dev.astler.unlib.ui.R
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib.utils.openAppInPlayStore
import dev.astler.unlib.utils.openPlayStoreDeveloperPage
import kotlinx.coroutines.launch
import java.util.* // ktlint-disable no-wildcard-imports

abstract class BaseUnLiActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    ActivityInterface {

    private var mReviewInfo: ReviewInfo? = null

    private val mReviewManager: ReviewManager by lazy {
        ReviewManagerFactory.create(this)
    }

    var mActiveFragment: Fragment? = null

    open var mAppMenuId = 0

    open fun updateNavigationMenu() {}

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppSettings.loadLocale(newBase))
    }

    override fun setCurrentFragment(fragment: Fragment) {
        mActiveFragment = fragment

        lifecycleScope.launch {
            val nStartCounter = UnliApp.getInstance().mAppStartCounter
            val nLastRequestCounter = LocalStorage.reviewLastRequest()

            if (isReviewLaunchAvailable() && nStartCounter >= 3 && nLastRequestCounter >= 300000) {
                mReviewInfo?.let { it1 ->
                    mReviewManager.launchReviewFlow(this@BaseUnLiActivity, it1)
                    LocalStorage.setReviewRequestTime(GregorianCalendar().timeInMillis)
                }
            }
        }
    }

    open fun isReviewLaunchAvailable(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UnliApp.getInstance().initAppLanguage(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        loadTheme(R.style.AppUnliTheme, R.style.AppUnliDarkTheme)

        mReviewManager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                mReviewInfo = request.result
                infoLog(mReviewInfo.toString())
            } else {
                request.exception?.message?.let { infoLog(it) }
            }
        }
    }

    fun loadTheme(@StyleRes lightThemeId: Int, @StyleRes darkThemeId: Int) {
        lifecycleScope.launch {
            var nCurrentTheme = LocalStorage.appTheme()

            LocalStorage.appThemeWatcher {
                infoLog("app theme = $it")

                if (it == cSystemDefault) {
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES -> setTheme(darkThemeId)
                        Configuration.UI_MODE_NIGHT_NO -> setTheme(lightThemeId)
                    }
                } else {
                    if (it == cDark) {
                        setTheme(darkThemeId)
                    } else {
                        setTheme(lightThemeId)
                    }
                }

                if (nCurrentTheme != it) {
                    nCurrentTheme = it
                    recreate()
                }
            }
        }
    }

    open fun navigationViewInflateMenus(navigationView: NavigationView) {
        navigationView.menu.clear()

        if (mAppMenuId != 0)
            navigationView.inflateMenu(mAppMenuId)

        navigationView.inflateMenu(R.menu.base_activity_drawer)
    }

    override fun showInterstitialAd() {}
    override fun showRewardAd() {}

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rate_app -> this.openAppInPlayStore()
            R.id.more_apps -> this.openPlayStoreDeveloperPage()
            R.id.settings -> navToSettingsFragment()
            R.id.about -> navToAboutFragment()
        }

        return true
    }

    open fun navToSettingsFragment() {}
    open fun navToAboutFragment() {}

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (UnliApp.getInstance().mAppTheme == cSystemDefault) {
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

    override fun setToolbarTitle(title: String) {}

    override fun backPressed(endAction: () -> Unit) {}
}
