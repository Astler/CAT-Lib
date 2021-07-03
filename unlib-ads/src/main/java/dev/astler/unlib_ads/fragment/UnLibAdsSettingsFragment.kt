package dev.astler.unlib_ads.fragment

import android.os.Bundle
import androidx.preference.Preference
import dev.astler.unlib.ui.R
import dev.astler.unlib.ui.fragment.UnLibSettingsFragment
import dev.astler.unlib_ads.utils.canShowAds

open class UnLibAdsSettingsFragment : UnLibSettingsFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)

        val mPreference: Preference? = findPreference("dayWithoutAds")

        mPreference?.setOnPreferenceClickListener {
            coreListener.showRewardAd()
            true
        }

        mPreference?.isVisible = requireContext().canShowAds()
    }
}
