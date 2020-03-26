package dev.astler.unli.interfaces

import android.content.Context
import android.view.MenuItem
import androidx.fragment.app.Fragment

interface ActivityInterface {
    fun backPressed(withEndAction: () -> Unit = {})
    fun setCurrentFragment(fragment:Fragment)
    fun getActivityContext(): Context
}