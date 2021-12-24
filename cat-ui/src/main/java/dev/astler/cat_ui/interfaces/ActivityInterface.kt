package dev.astler.cat_ui.interfaces

import androidx.fragment.app.Fragment

interface ActivityInterface : CoreFragmentInterface {
    fun setCurrentFragment(fragment: Fragment)

    fun setToolbarTitle(title: String)
    fun setToolbarElevationEnabled(pElevationEnabled: Boolean) {}
    fun setToolbarVisibility(pIsToolbarVisible: Boolean) {}
}
