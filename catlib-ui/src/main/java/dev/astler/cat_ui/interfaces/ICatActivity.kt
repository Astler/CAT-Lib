package dev.astler.cat_ui.interfaces

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

interface ICatActivity : CoreFragmentInterface {
    fun setCurrentFragment(fragment: Fragment)
    fun setToolbarTitle(title: String)
    fun callBackPressed()
    fun setupToolbar(toolbar: Toolbar) {}
}
