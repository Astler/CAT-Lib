package dev.astler.unli.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.astler.unli.R
import dev.astler.unli.interfaces.ActivityInterface

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