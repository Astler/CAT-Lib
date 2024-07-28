package dev.astler.unlib_test.fragments

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.fragments.CatDefaultSettingsFragment
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class SettingsFragment : CatDefaultSettingsFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyInsetter {
            type(statusBars = true, navigationBars = true) {
                margin()
            }
        }
    }
}