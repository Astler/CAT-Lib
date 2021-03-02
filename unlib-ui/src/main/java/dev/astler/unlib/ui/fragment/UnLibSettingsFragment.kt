package dev.astler.unlib.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.astler.unlib.ui.R
import dev.astler.unlib.ui.interfaces.ActivityInterface
import dev.astler.unlib.utils.canShowAds

open class UnLibSettingsFragment : PreferenceFragmentCompat() {

    private var mPreference: Preference? = null
    lateinit var coreListener: ActivityInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        coreListener = context as ActivityInterface
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)

        mPreference = findPreference("dayWithoutAds")

        mPreference?.setOnPreferenceClickListener {
            coreListener.showRewardAd()
            true
        }

        if (!requireContext().canShowAds()) {
            mPreference?.isVisible = false
        }
    }

    override fun onResume() {
        super.onResume()
        coreListener.setToolbarTitle(getString(R.string.settings))
    }
}