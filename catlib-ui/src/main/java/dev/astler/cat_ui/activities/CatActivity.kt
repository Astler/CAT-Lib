package dev.astler.cat_ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.zeugmasolutions.localehelper.Locales
import dev.astler.cat_ui.appResumeTime
import dev.astler.cat_ui.StartTimeKey
import dev.astler.cat_ui.fragments.IInternetDependentFragment
import dev.astler.cat_ui.interfaces.ICatActivity
import dev.astler.cat_ui.utils.getDimensionFromAttr
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.RemoteConfigProvider
import dev.astler.catlib.gPreferencesTool
import dev.astler.catlib.getDefaultNightMode
import dev.astler.catlib.remote_config.IRemoteConfigListener
import dev.astler.catlib.utils.errorLog
import dev.astler.catlib.utils.isOnline
import dev.astler.catlib.utils.isPlayStoreInstalled
import java.util.GregorianCalendar
import java.util.Locale
import javax.inject.Inject

abstract class CatActivity :
    LocaleAwareCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    ICatActivity, IRemoteConfigListener {

    private var _currentWindowInsets: WindowInsetsCompat = WindowInsetsCompat.Builder().build()
    private var _topInsets = 0
    private var _bottomInsets = 0
    private var _toolbarHeight = 0

    @Inject
    lateinit var _remoteConfig: RemoteConfigProvider

    protected var mActiveFragment: Fragment? = null

    protected var reviewInfo: ReviewInfo? = null
    protected val reviewManager: ReviewManager by lazy {
        ReviewManagerFactory.create(this)
    }

    public fun getRemoteConfig(): RemoteConfigProvider = _remoteConfig

    override fun onFetchCompleted() {

    }

    private val _connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val _networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                onInternetConnected(network)
                if (mActiveFragment is IInternetDependentFragment) {
                    lifecycleScope.launchWhenResumed {
                        (mActiveFragment as IInternetDependentFragment).onInternetAvailable()
                    }
                }
            }

            override fun onLost(network: Network) {
                onInternetLost(network)
                if (mActiveFragment is IInternetDependentFragment) {
                    lifecycleScope.launchWhenResumed {
                        (mActiveFragment as IInternetDependentFragment).onInternetLost()
                    }
                }
            }
        }
    }

    override fun callBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())
        delegate.applyDayNight()

        _remoteConfig.getBoolean("bool_test")

        _toolbarHeight = getDimensionFromAttr(androidx.appcompat.R.attr.actionBarSize)

        gPreferencesTool.loadDefaultPreferences(this)

        gPreferencesTool.edit(StartTimeKey, GregorianCalendar().timeInMillis)

        if (gPreferencesTool.isFirstStart) {
            onFirstAppStart()
            gPreferencesTool.isFirstStart = false
        }

        if (gPreferencesTool.isFirstStartForVersion(appVersionCode())) {
            gPreferencesTool.edit(
                PreferencesTool.appFirstStartTime,
                GregorianCalendar().timeInMillis
            )

            onFirstStartCurrentVersion()

            gPreferencesTool.setFirstStartForVersion(appVersionCode())
        }

        _connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            _networkCallback
        )

        if (isPlayStoreInstalled()) {
            reviewManager.requestReviewFlow().addOnCompleteListener { request ->
                if (request.isSuccessful) {
                    reviewInfo = request.result
                } else {
                    errorLog(request.exception, "error during requestReviewFlow")
                }
            }
        }
    }

    /**
     * UI methods
     */

    override fun getTopPadding() = _topInsets

    override fun getBottomPadding() = _bottomInsets

    override fun getToolbarHeight() = _toolbarHeight

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

    override fun onDestroy() {
        super.onDestroy()
        _connectivityManager.unregisterNetworkCallback(_networkCallback)
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
                    "uk" -> Locales.Ukrainian
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

            reviewInfo?.let { it1 ->
                reviewManager.launchReviewFlow(this, it1)
                gPreferencesTool.appResumeTime = GregorianCalendar().timeInMillis
            }
        }

        if (fragment is IInternetDependentFragment) {
            if (isOnline()) {
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
