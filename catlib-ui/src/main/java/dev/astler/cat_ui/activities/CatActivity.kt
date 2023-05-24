package dev.astler.cat_ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.zeugmasolutions.localehelper.Locales
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.StartTimeKey
import dev.astler.cat_ui.appResumeTime
import dev.astler.cat_ui.fragments.IInternetDependentFragment
import dev.astler.cat_ui.interfaces.ICatActivity
import dev.astler.cat_ui.interfaces.IRootInsets
import dev.astler.cat_ui.utils.getDimensionFromAttr
import dev.astler.catlib.getDefaultNightMode
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.IRemoteConfigListener
import dev.astler.catlib.remote_config.RemoteConfigProvider
import dev.astler.catlib.utils.errorLog
import dev.astler.catlib.utils.isOnline
import dev.astler.catlib.utils.isPlayStoreInstalled
import kotlinx.coroutines.launch
import java.util.GregorianCalendar
import java.util.Locale
import javax.inject.Inject

abstract class CatActivity<T : ViewBinding> : LocaleAwareCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    ICatActivity, IRootInsets, IRemoteConfigListener {

    @Inject
    protected lateinit var preferencesTool: PreferencesTool

    @Inject
    protected lateinit var remoteConfig: RemoteConfigProvider

    protected lateinit var binding: T
        private set

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

    protected var reviewInfo: ReviewInfo? = null
    protected val reviewManager: ReviewManager by lazy {
        ReviewManagerFactory.create(this)
    }

    override fun onFetchCompleted() {

    }

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

    protected abstract fun inflateBinding(layoutInflater: LayoutInflater): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())

        delegate.applyDayNight()

        _toolbarHeight = getDimensionFromAttr(androidx.appcompat.R.attr.actionBarSize)

        preferencesTool.loadDefaultPreferences(this)

        preferencesTool.edit(StartTimeKey, GregorianCalendar().timeInMillis)

        if (preferencesTool.isFirstStart) {
            onFirstAppStart()
            preferencesTool.isFirstStart = false
        }

        if (preferencesTool.isFirstStartForVersion(appVersionCode())) {
            preferencesTool.edit(
                PreferencesTool.appFirstStartTime,
                GregorianCalendar().timeInMillis
            )

            onFirstStartCurrentVersion()

            preferencesTool.setFirstStartForVersion(appVersionCode())
        }

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
        preferencesTool.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferencesTool.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        preferencesTool.appResumeTime = GregorianCalendar().timeInMillis
        preferencesTool.appReviewTime = GregorianCalendar().timeInMillis

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
            AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())
            delegate.applyDayNight()
            return
        }

        if (key == PreferencesTool.appLocaleKey) {
            updateLocale(
                when (preferencesTool.appLanguage) {
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
        activeFragment = fragment

        val nAppReviewTime =
            GregorianCalendar().timeInMillis - preferencesTool.appResumeTime

        if (nAppReviewTime >= 200000) {

            reviewInfo?.let { it1 ->
                reviewManager.launchReviewFlow(this, it1)
                preferencesTool.appResumeTime = GregorianCalendar().timeInMillis
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
