package dev.astler.cat_ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.astler.cat_ui.databinding.FragmentViewPager2Binding
import dev.astler.cat_ui.utils.views.setStatusPaddingForView
import dev.astler.unlib.gPreferencesTool

abstract class CatViewPagerFragment : CatFragment<FragmentViewPager2Binding>() {

    open val mSelectedPageSaveName: String = "selected_page"
    open val mSelectedDefaultValue: Int = 0

    abstract fun getAdapter(): FragmentStateAdapter

    private val mFragmentWorldEditBinding by viewBinding<FragmentViewPager2Binding>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentViewPager2Binding
        get() = FragmentViewPager2Binding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(mFragmentWorldEditBinding) {
            viewPager.adapter = getAdapter()

            val nLastChosenItem =
                gPreferencesTool.getInt(mSelectedPageSaveName, mSelectedDefaultValue)

            viewPager.setCurrentItem(nLastChosenItem, false)

            viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    gPreferencesTool.edit(mSelectedPageSaveName, position)
                }
            })

            TabLayoutMediator(tabView, viewPager) { tab, position ->
                setLayoutMediator(tab, position)
            }.attach()

            viewPager.offscreenPageLimit = 1

            root.setStatusPaddingForView()
        }
    }

    open fun setLayoutMediator(tab: TabLayout.Tab, position: Int) {

    }

    fun setPageTransformer(mPageTransformer: ViewPager2.PageTransformer? = null) {
        mFragmentWorldEditBinding.viewPager.setPageTransformer(mPageTransformer)
    }

    fun openPage(pPage: Int, pSmooth: Boolean = true) {
        mFragmentWorldEditBinding.viewPager.setCurrentItem(pPage, pSmooth)
    }

    fun firstPage(pSmooth: Boolean = true) {
        openPage(0, pSmooth)
    }
}
