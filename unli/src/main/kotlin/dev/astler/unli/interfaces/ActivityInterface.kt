package dev.astler.unli.interfaces

import androidx.fragment.app.Fragment

interface ActivityInterface {
    fun backPressed(endAction: () -> Unit = {})
    fun showInterstitialAd()
    fun showRewardAd()
    fun setCurrentFragment(fragment:Fragment)
    fun setToolbarTitle(title: String)

    fun setToolbarElevationEnabled(pElevationEnabled: Boolean) {}
    fun toggleToolbar(pIsToolbarVisible: Boolean) {}
}