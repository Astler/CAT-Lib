package dev.astler.ui.interfaces

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import dev.astler.catlib.preferences.PreferencesTool

interface ICatActivity : CoreFragmentInterface {
    val preferences: PreferencesTool

    fun setCurrentFragment(fragment: Fragment)
    fun callBackPressed()
    fun setToolbarTitle(title: Any?) {}
    fun setupToolbar(toolbar: Toolbar) {}
}
