package dev.astler.unlib.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dev.astler.unlib.AppSettings
import dev.astler.unlib.PreferencesTool
import dev.astler.unlib.UnliApp
import dev.astler.unlib.data.RemoteConfig
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.interfaces.ActivityInterface
import dev.astler.unlib.ui.R
import dev.astler.unlib.utils.* // ktlint-disable no-wildcard-imports
import java.util.* // ktlint-disable no-wildcard-imports

abstract class BaseUnLiActivity(pLayoutId: Int = 0) :
    AppCompatActivity(pLayoutId),
    NavigationView.OnNavigationItemSelectedListener,
    SharedPreferences.OnSharedPreferenceChangeListener,
    ActivityInterface {

    open val mConfigAppPackage: String by lazy {
        packageName.replace(".", "_")
    }

    lateinit var mRemoteConfig: RemoteConfig

    protected var mReviewInfo: ReviewInfo? = null
    protected val mReviewManager: ReviewManager by lazy {
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

        val nAppReviewTime =
            GregorianCalendar().timeInMillis - gPreferencesTool.appReviewTime

        if (isReviewLaunchAvailable() && nAppReviewTime >= 200000) {

            mReviewInfo?.let { it1 ->
                mReviewManager.launchReviewFlow(this, it1)
                gPreferencesTool.appReviewTime = GregorianCalendar().timeInMillis
            }
        }
    }

    open fun isReviewLaunchAvailable(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRemoteConfig = RemoteConfig.getInstance()

        UnliApp.getInstance().initAppLanguage(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        setDefaultPreferences()

        gPreferencesTool.edit("start_time", GregorianCalendar().timeInMillis)

        loadTheme(R.style.AppUnliTheme, R.style.AppUnliDarkTheme)

        mReviewManager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                mReviewInfo = request.result
                infoLog(mReviewInfo.toString())
            } else {
                request.exception?.message?.let { infoLog(it) }
            }
        }

        gPreferencesTool.appReviewTime = GregorianCalendar().timeInMillis

        checkVersion()
    }

    private fun checkVersion() {
        mRemoteConfig.fetchData(object : RemoteConfig.OnFetchComplete {
            override fun onComplete() {
                simpleTry {
                    val nInfo = packageManager.getPackageInfo(packageName, 0)
                    val nVersion = (
                        if (isP()) {
                            nInfo.longVersionCode
                        } else {
                            nInfo.versionCode
                        }
                        ).toLong()

                    if (mRemoteConfig.mAppVersion != 0L && nVersion < mRemoteConfig.mAppVersion) {
                        showDialogUpdateApp()
                    }
                }
            }
        })
    }

    open fun showDialogUpdateApp() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.new_version_title)
        builder.setMessage(R.string.new_version_text)
        builder.setPositiveButton(R.string.UPDATE) { dialog, _ ->
            dialog.cancel()
            openAppInPlayStore()
        }
        builder.setNegativeButton(R.string.close) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    open fun setDefaultPreferences() {
        gPreferencesTool.loadDefaultPreferences(this)
    }

    fun loadTheme(@StyleRes lightThemeId: Int, @StyleRes darkThemeId: Int) {
        if (gPreferencesTool.mIsSystemTheme) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> setTheme(darkThemeId)
                Configuration.UI_MODE_NIGHT_NO -> setTheme(lightThemeId)
            }
        } else {
            if (gPreferencesTool.mIsDarkTheme) {
                setTheme(darkThemeId)
            } else {
                setTheme(lightThemeId)
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if (key == PreferencesTool.appThemeKey) {
            recreate()
        }

        if (key == PreferencesTool.appLocaleKey) {
            recreate()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val preferencesTool = PreferencesTool(this)

        if (preferencesTool.mIsSystemTheme) {
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
        gPreferencesTool.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        gPreferencesTool.unregisterListener(this)
    }

    override fun setToolbarTitle(title: String) {}

    override fun backPressed(endAction: () -> Unit) {}
}
