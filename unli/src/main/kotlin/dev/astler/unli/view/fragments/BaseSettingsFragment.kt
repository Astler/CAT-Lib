package dev.astler.unli.view.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import dev.astler.unli.R

open class BaseSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)

    }

}