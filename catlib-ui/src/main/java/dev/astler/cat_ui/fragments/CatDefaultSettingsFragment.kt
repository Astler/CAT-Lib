package dev.astler.cat_ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.interfaces.ICatActivity
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.ui.R
import javax.inject.Inject

@AndroidEntryPoint
open class CatDefaultSettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var preferences: PreferencesTool

    private var noAdsItem: Preference? = null
    protected var coreListener: ICatActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ICatActivity)
            coreListener = context
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(dev.astler.catlib.core.R.xml.prefs)

        noAdsItem = findPreference("dayWithoutAds")

//        mPreference?.setOnPreferenceClickListener {
//            coreListener.showRewardAd()
//            true
//        }

//        noAdsItem?.isVisible = requireContext().canShowAds(preferences)
    }

    override fun onResume() {
        super.onResume()
        coreListener?.setToolbarTitle(getString(gg.pressf.resources.R.string.settings))
    }
}
