package dev.astler.unli.interfaces

import android.content.Context
import android.view.MenuItem
import androidx.fragment.app.Fragment

interface ActivityInterface {
    fun backPressed(endAction: () -> Unit = {})
    fun showInterstitialAd()
    fun showRewardAd()
    fun setCurrentFragment(fragment:Fragment)
    fun setToolbarTitle(title: String)
    fun getActivityContext(): Context
}