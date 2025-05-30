package dev.astler.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import dev.astler.catlib.analytics.CatAnalytics
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.extensions.defaultNightMode
import dev.astler.catlib.localization.LocalizationManager
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.IRemoteConfigListener
import dev.astler.catlib.remote_config.RemoteConfigProvider
import dev.astler.ui.StartTimeKey
import dev.astler.ui.appResumeTime
import dev.astler.ui.interfaces.ICatActivity
import dev.astler.ui.utils.tryToGetTextFrom
import java.util.GregorianCalendar
import javax.inject.Inject

abstract class CatActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener,
    ICatActivity, IRemoteConfigListener {

    @Inject
    override lateinit var preferences: PreferencesTool

    @Inject
    lateinit var analytics: CatAnalytics

    @Inject
    lateinit var appConfig: AppConfig

    @Inject
    lateinit var remoteConfig: RemoteConfigProvider

    @Inject
    lateinit var localizationManager: LocalizationManager

    private var _activeFragment: Fragment? = null

    val activeFragment: Fragment?
        get() = _activeFragment

    private val onFragmentChangedListeners = mutableListOf<(Fragment) -> Unit>()

    fun addOnFragmentChangedListener(listener: (Fragment) -> Unit) {
        onFragmentChangedListeners.add(listener)
    }

    fun removeOnFragmentChangedListener(listener: (Fragment) -> Unit) {
        onFragmentChangedListeners.remove(listener)
    }

    private fun notifyFragmentChanged(fragment: Fragment) {
        for (listener in onFragmentChangedListeners) {
            listener(fragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(preferences.defaultNightMode)
        delegate.applyDayNight()

        preferences.loadDefaultPreferences(this)
        preferences.edit(StartTimeKey, GregorianCalendar().timeInMillis)

        if (preferences.isFirstStart) {
            onFirstAppStart()
            preferences.isFirstStart = false
        }

        if (preferences.isFirstStartForVersion(appVersionCode())) {
            preferences.appFirstStartTime = GregorianCalendar().timeInMillis
            onFirstStartCurrentVersion()
            preferences.setFirstStartForVersion(appVersionCode())
        }
    }

    override fun onFetchCompleted() {}

    override fun callBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    protected open fun appVersionCode() = 0

    protected open fun onFirstAppStart() {}

    protected open fun onFirstStartCurrentVersion() {}

    override fun onStart() {
        super.onStart()
        preferences.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferences.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        preferences.appResumeTime = GregorianCalendar().timeInMillis
        preferences.appReviewTime = GregorianCalendar().timeInMillis
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            PreferencesTool.appThemeKey -> {
                AppCompatDelegate.setDefaultNightMode(preferences.defaultNightMode)
                delegate.applyDayNight()
            }
            PreferencesTool.appLocaleKey -> {
                val newLocale = sharedPreferences?.getString(key, LocalizationManager.SYSTEM_LANGUAGE)
                    ?: LocalizationManager.SYSTEM_LANGUAGE
                localizationManager.setAppLocale(newLocale)
            }
        }
    }

    fun changeLanguage(languageCode: String) {
        preferences.edit(PreferencesTool.appLocaleKey, languageCode)
    }

    override fun setCurrentFragment(fragment: Fragment) {
        _activeFragment = fragment
        notifyFragmentChanged(fragment)
    }

    override fun setToolbarTitle(title: Any?) {
        supportActionBar?.title = tryToGetTextFrom(title)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }
}