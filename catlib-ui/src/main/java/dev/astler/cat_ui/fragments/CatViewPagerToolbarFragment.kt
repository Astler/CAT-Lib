package dev.astler.cat_ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.databinding.FragmentViewPager2ToolbarBinding
import dev.astler.cat_ui.utils.setStatusBarColor
import dev.astler.cat_ui.utils.views.setStatusPaddingForView

@AndroidEntryPoint
abstract class CatViewPagerToolbarFragment : CatFragment<FragmentViewPager2ToolbarBinding>() {

    abstract val pagerAdapter: FragmentStateAdapter
    abstract val pagesNames: List<String>

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentViewPager2ToolbarBinding
        get() = FragmentViewPager2ToolbarBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setStatusBarColor(android.R.color.transparent)

        with(binding) {
            fab.visibility = View.GONE

            ViewCompat.requestApplyInsets(binding.coordinator)

            coreListener?.setupToolbar(toolbarLayout.toolbar)

            pager.adapter = pagerAdapter

            TabLayoutMediator(tabs, pager) { tab, position ->
                tab.text = pagesNames[position]
            }.attach()

            pager.offscreenPageLimit = 1

            root.setStatusPaddingForView()
        }
    }
}