package dev.astler.cat_ui.activities

import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.zeugmasolutions.localehelper.Locales
import dev.astler.cat_ui.appResumeTime
import dev.astler.cat_ui.cStartTime
import dev.astler.cat_ui.interfaces.ActivityInterface
import dev.astler.unlib.PreferencesTool
import dev.astler.unlib.data.RemoteConfig
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.getDefaultNightMode
import dev.astler.unlib.utils.infoLog
import java.util.*

abstract class CatActivity :
    LocaleAwareCompatActivity(),
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

    override fun callBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)

        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())
        delegate.applyDayNight()

        mRemoteConfig = RemoteConfig.getInstance()

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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PreferencesTool.appThemeKey) {
            AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())
            delegate.applyDayNight()
            return
        }

        if (key == PreferencesTool.appLocaleKey) {
            updateLocale(
                when (gPreferencesTool.appLanguage) {
                    "ru" -> Locales.Russian
                    "ua" -> Locales.Ukrainian
                    "en" -> Locales.English
                    else -> ConfigurationCompat.getLocales(Resources.getSystem().configuration)
                        .get(0) ?: Locale.ENGLISH
                }
            )
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
}
