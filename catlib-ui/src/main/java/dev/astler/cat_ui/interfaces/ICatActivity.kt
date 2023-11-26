package dev.astler.cat_ui.interfaces

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import dev.astler.catlib.preferences.PreferencesTool

interface ICatActivity : CoreFragmentInterface {
    val preferences: PreferencesTool

    fun setCurrentFragment(fragment: Fragment)
    fun setToolbarTitle(title: String)
    fun callBackPressed()
    fun setupToolbar(toolbar: Toolbar) {}
}
