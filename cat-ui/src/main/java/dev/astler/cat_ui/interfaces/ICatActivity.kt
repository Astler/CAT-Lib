package dev.astler.cat_ui.interfaces

import androidx.fragment.app.Fragment

interface ICatActivity : CoreFragmentInterface {
    fun setCurrentFragment(fragment: Fragment)
    fun setToolbarTitle(title: String)
    fun callBackPressed()
    fun getTopPadding(): Int
    fun getBottomPadding(): Int
    fun getToolbarHeight(): Int
}
