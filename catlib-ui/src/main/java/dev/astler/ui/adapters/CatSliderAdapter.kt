package dev.astler.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class CatSliderAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    protected abstract val fragmentsArray: Array<Fragment>

    private val sliderPages by lazy {
        fragmentsArray
    }

    override fun getItemCount() = sliderPages.size

    override fun createFragment(position: Int) = sliderPages[position]
}
