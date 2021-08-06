package dev.astler.unlib.interfaces

import androidx.fragment.app.Fragment

interface ActivityInterface {
    fun backPressed(endAction: () -> Unit = {})
    fun setCurrentFragment(fragment: Fragment)
    fun setToolbarTitle(title: String)

    fun setToolbarElevationEnabled(pElevationEnabled: Boolean) {}
    fun toggleToolbar(pIsToolbarVisible: Boolean) {}
}
