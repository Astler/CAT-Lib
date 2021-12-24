package dev.astler.cat_ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dev.astler.cat_ui.appResumeTime
import dev.astler.cat_ui.cStartTime
import dev.astler.unlib.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.data.RemoteConfig
import dev.astler.unlib.utils.infoLog
import java.util.* // ktlint-disable no-wildcard-imports

interface ActivityInterface {
    fun backPressed(endAction: () -> Unit = {})
    fun setCurrentFragment(fragment: Fragment)
    fun setToolbarTitle(title: String)

    fun setToolbarElevationEnabled(pElevationEnabled: Boolean) {}
    fun toggleToolbar(pIsToolbarVisible: Boolean) {}
}

abstract class CatActivity(pLayoutId: Int = 0) :
    AppCompatActivity(pLayoutId),
    SharedPreferences.OnSharedPreferenceChangeListener,
    ActivityInterface {

    lateinit var mRemoteConfig: RemoteConfig
    protected var mActiveFragment: Fragment? = null

    protected var mReviewInfo: ReviewInfo? = null
    protected val mReviewManager: ReviewManager by lazy {
        ReviewManagerFactory.create(this)
    }

    open val mConfigAppPackage: String by lazy {
        packageName.replace(".", "_")
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppSettings.loadLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRemoteConfig = RemoteConfig.getInstance()

        UnliApp.getInstance().initAppLanguage(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        gPreferencesTool.loadDefaultPreferences(this)

        gPreferencesTool.edit(cStartTime, GregorianCalendar().timeInMillis)

        mReviewManager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                mReviewInfo = request.result
                infoLog(mReviewInfo.toString())
            } else {
                request.exception?.message?.let { infoLog(it) }
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

    override fun onResume() {
        super.onResume()
        gPreferencesTool.appResumeTime = GregorianCalendar().timeInMillis
        gPreferencesTool.appReviewTime = GregorianCalendar().timeInMillis
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if (key == PreferencesTool.appThemeKey) {
            AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())
            recreate()
        }

        if (key == PreferencesTool.appLocaleKey) {
            recreate()
        }
    }

    override fun setCurrentFragment(fragment: Fragment) {
        mActiveFragment = fragment

        val nAppReviewTime =
            GregorianCalendar().timeInMillis - gPreferencesTool.appResumeTime

        if (nAppReviewTime >= 200000) {

            mReviewInfo?.let { it1 ->
                mReviewManager.launchReviewFlow(this, it1)
                gPreferencesTool.appResumeTime = GregorianCalendar().timeInMillis
            }
        }
    }

    override fun setToolbarTitle(title: String) {}

    override fun backPressed(endAction: () -> Unit) {}
}
