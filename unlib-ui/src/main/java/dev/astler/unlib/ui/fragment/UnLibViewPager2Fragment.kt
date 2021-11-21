package dev.astler.unlib.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.ui.R
import dev.astler.unlib.ui.databinding.FragmentViewPager2Binding
import dev.astler.unlib.utils.views.setStatusPaddingForView

abstract class UnLibViewPager2Fragment(pLayoutId: Int = R.layout.fragment_view_pager2) : UnLibFragment(pLayoutId) {

    open val mSelectedPageSaveName: String = "selected_page"
    open val mSelectedDefaultValue: Int = 0

    abstract fun getAdapter(): FragmentStateAdapter

    private val mFragmentWorldEditBinding by viewBinding<FragmentViewPager2Binding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(mFragmentWorldEditBinding) {
            viewPager.adapter = getAdapter()

            val nLastChosenItem = gPreferencesTool.getInt(mSelectedPageSaveName, mSelectedDefaultValue)

            viewPager.setCurrentItem(nLastChosenItem, false)

            viewPager.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)

                        gPreferencesTool.edit(mSelectedPageSaveName, position)
                    }
                })

            TabLayoutMediator(tabView, viewPager) { tab, position ->
            }.attach()

            viewPager.offscreenPageLimit = 1

            root.setStatusPaddingForView()
        }
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
