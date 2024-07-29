package dev.astler.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.activities.LibsActivity
import dev.astler.ui.interfaces.ICatActivity
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.preferences.PreferencesTool
import javax.inject.Inject

@AndroidEntryPoint
open class CatDefaultSettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var appConfig: AppConfig

    @Inject
    lateinit var preferences: PreferencesTool

    private var noAdsItem: Preference? = null
    private var privacy: Preference? = null
    protected var coreListener: ICatActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ICatActivity)
            coreListener = context
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(dev.astler.catlib.core.R.xml.prefs)

        noAdsItem = findPreference("dayWithoutAds")
        privacy = findPreference("privacy")

        privacy?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appConfig.policyLink))
            startActivity(intent)
            true
        }

        findPreference<Preference>("about_libraries")?.setOnPreferenceClickListener {
            activity?.startActivity(Intent(activity, LibsActivity::class.java))
            true
        }

//        noAdsItem?.isVisible = requireContext().canShowAds(preferences)
    }

    override fun onResume() {
        super.onResume()
        coreListener?.setToolbarTitle(getString(gg.pressf.resources.R.string.settings))
    }
}
