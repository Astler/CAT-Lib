package dev.astler.unlib.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.astler.unlib.interfaces.ActivityInterface
import dev.astler.unlib.ui.R

open class UnLibSettingsFragment : PreferenceFragmentCompat() {

    lateinit var coreListener: ActivityInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        coreListener = context as ActivityInterface
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)

        val mPreference: Preference? = findPreference("dayWithoutAds")
        mPreference?.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        coreListener.setToolbarTitle(getString(R.string.settings))
    }
}
