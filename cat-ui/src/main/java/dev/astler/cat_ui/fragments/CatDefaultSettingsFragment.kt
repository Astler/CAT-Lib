package dev.astler.cat_ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.astler.cat_ui.R
import dev.astler.cat_ui.interfaces.ActivityInterface
import dev.astler.unlib.utils.canShowAds

open class CatDefaultSettingsFragment : PreferenceFragmentCompat() {

    private var mPreference: Preference? = null
    lateinit var coreListener: ActivityInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        coreListener = context as ActivityInterface
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)

        mPreference = findPreference("dayWithoutAds")

//        mPreference?.setOnPreferenceClickListener {
//            coreListener.showRewardAd()
//            true
//        }

        mPreference?.isVisible = requireContext().canShowAds()
    }

    override fun onResume() {
        super.onResume()
        coreListener.setToolbarTitle(getString(R.string.settings))
    }
}
