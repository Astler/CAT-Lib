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
import dev.astler.cat_ui.cStartTime
import dev.astler.cat_ui.fragments.IInternetDependentFragment
import dev.astler.cat_ui.interfaces.ActivityInterface
import dev.astler.unlib.PreferencesTool
import dev.astler.unlib.data.RemoteConfig
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.getDefaultNightMode
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib.utils.isOnline
import java.util.*

abstract class CatActivity :
    LocaleAwareCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    ActivityInterface {

    private var currentWindowInsets: WindowInsetsCompat = WindowInsetsCompat.Builder().build()

    lateinit var mRemoteConfig: RemoteConfig
    protected var mActiveFragment: Fragment? = null

    protected var mReviewInfo: ReviewInfo? = null
    protected val mReviewManager: ReviewManager by lazy {
        ReviewManagerFactory.create(this)
    }

    private val connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (mActiveFragment is IInternetDependentFragment) {
                    lifecycleScope.launchWhenResumed {
                        (mActiveFragment as IInternetDependentFragment).onInternetAvailable()
                    }
                }
            }

            override fun onLost(network: Network) {
                if (mActiveFragment is IInternetDependentFragment) {
                    lifecycleScope.launchWhenResumed {
                        (mActiveFragment as IInternetDependentFragment).onInternetLost()
                    }
                }
            }
        }
    }

    open val mConfigAppPackage: String by lazy {
        packageName.replace(".", "_")
    }

    override fun callBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    private var topInsets = 0
    private var bottomInsets = 0

    override fun getTopPadding() = topInsets
    override fun getBottomPadding() = bottomInsets

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

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            networkCallback
        )
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

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
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
            currentWindowInsets = windowInsets
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
        val insets = currentWindowInsets.getInsets(currentInsetTypeMask)

        topInsets = insets.top
        bottomInsets = insets.bottom

//        binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//            updateMargins(insets.left, insets.top, insets.right, insets.bottom)
//        }

        return WindowInsetsCompat.Builder()
            .setInsets(currentInsetTypeMask, insets)
            .build()
    }
}
