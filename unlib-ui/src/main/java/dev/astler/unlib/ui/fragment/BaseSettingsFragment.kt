package dev.astler.unlib.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.astler.unlib.ui.R
import dev.astler.unlib.ui.interfaces.ActivityInterface

open class BaseSettingsFragment : PreferenceFragmentCompat() {

    lateinit var coreListener: ActivityInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        coreListener = context as ActivityInterface
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)

        findPreference<Preference>("dayWithoutAds")?.setOnPreferenceClickListener {
            coreListener.showRewardAd()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        coreListener.setToolbarTitle(getString(R.string.settings))
    }
}