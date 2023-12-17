package dev.astler.cat_ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dev.astler.cat_ui.StartTimeKey
import dev.astler.cat_ui.appResumeTime
import dev.astler.cat_ui.fragments.IInternetDependentFragment
import dev.astler.cat_ui.interfaces.ICatActivity
import dev.astler.cat_ui.interfaces.IRootInsets
import dev.astler.cat_ui.utils.getDimensionFromAttr
import dev.astler.catlib.analytics.CatAnalytics
import dev.astler.catlib.extensions.defaultNightMode
import dev.astler.catlib.extensions.isOnline
import dev.astler.catlib.extensions.isPlayStoreInstalled
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.IRemoteConfigListener
import dev.astler.catlib.remote_config.RemoteConfigProvider
import kotlinx.coroutines.launch
import java.util.GregorianCalendar
import javax.inject.Inject

abstract class CatActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener,
    ICatActivity, IRootInsets, IRemoteConfigListener {

    @Inject
    override lateinit var preferences: PreferencesTool

    @Inject
    protected lateinit var analytics: CatAnalytics

    @Inject
    protected lateinit var remoteConfig: RemoteConfigProvider

    private var _currentWindowInsets: WindowInsetsCompat = WindowInsetsCompat.Builder().build()

    private var _topInsets = 0
    private var _bottomInsets = 0
    private var _toolbarHeight = 0

    override val topPadding: Int
        get() = _topInsets

    override val bottomPadding: Int
        get() = _bottomInsets

    override val toolbarHeight: Int
        get() = _toolbarHeight

    protected var activeFragment: Fragment? = null

    @Suppress("MemberVisibilityCanBePrivate")
    protected var reviewInfo: ReviewInfo? = null

    @Suppress("MemberVisibilityCanBePrivate")
    protected val reviewManager: ReviewManager by lazy {
        ReviewManagerFactory.create(this)
    }

    override fun onFetchCompleted() {}

    private val _connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val _networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                onInternetConnected(network)
                if (activeFragment is IInternetDependentFragment) {
                    lifecycleScope.launch {
                        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                            (activeFragment as IInternetDependentFragment).onInternetAvailable()
                        }
                    }
                }
            }

            override fun onLost(network: Network) {
                onInternetLost(network)
                if (activeFragment is IInternetDependentFragment) {
                    lifecycleScope.launch {
                        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                            (activeFragment as IInternetDependentFragment).onInternetLost()
                        }
                    }
                }
            }
        }
    }

    override fun callBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(preferences.defaultNightMode)
        delegate.applyDayNight()

        _toolbarHeight = getDimensionFromAttr(androidx.appcompat.R.attr.actionBarSize)

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

        with(GoogleApiAvailability.getInstance()) {
            if (isPlayStoreInstalled()) {
                try {
                    reviewManager.requestReviewFlow().addOnCompleteListener { request ->
                        if (request.isSuccessful) {
                            reviewInfo = request.result
                        } else {
                            errorLog(request.exception, "error during requestReviewFlow")
                        }
                    }

                } catch (e: ReviewException) {
                    errorLog(e, "error during requestReviewFlow")
                }
            }
        }

    }

    /**
     * My personal use methods
     */

    protected open fun appVersionCode() = 0

    protected open fun onInternetConnected(network: Network) {}

    protected open fun onInternetLost(network: Network) {}

    protected open fun onFirstAppStart() {}

    protected open fun onFirstStartCurrentVersion() {}

    /**
     * System callbacks
     */

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

        _connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            _networkCallback
        )
    }

    override fun onPause() {
        super.onPause()
        _connectivityManager.unregisterNetworkCallback(_networkCallback)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PreferencesTool.appThemeKey) {
            AppCompatDelegate.setDefaultNightMode(preferences.defaultNightMode)
            delegate.applyDayNight()
            return
        }

        if (key == PreferencesTool.appLocaleKey) {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(sharedPreferences?.getString(key, "en"))
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }

    override fun setCurrentFragment(fragment: Fragment) {
        activeFragment = fragment

        val nAppReviewTime =
            GregorianCalendar().timeInMillis - preferences.appResumeTime

        if (nAppReviewTime >= 200000) {

            reviewInfo?.let { it1 ->
                reviewManager.launchReviewFlow(this, it1)
                preferences.appResumeTime = GregorianCalendar().timeInMillis
            }
        }

        if (fragment is IInternetDependentFragment) {
            if (isOnline) {
                fragment.onInternetAvailable()
            } else {
                fragment.onInternetLost()
            }
        }
    }

    override fun setToolbarTitle(title: String) {}

    protected fun loadInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            _currentWindowInsets = windowInsets
            applyInsets()
        }
    }

    private fun applyInsets(): WindowInsetsCompat {
        val currentInsetTypeMask = mutableListOf(
            WindowInsetsCompat.Type.navigationBars(),
            WindowInsetsCompat.Type.statusBars()
        ).fold(0) { accumulator, type ->
            accumulator or type
        }
        val insets = _currentWindowInsets.getInsets(currentInsetTypeMask)

        _topInsets = insets.top
        _bottomInsets = insets.bottom

        return WindowInsetsCompat.Builder()
            .setInsets(currentInsetTypeMask, insets)
            .build()
    }
}
