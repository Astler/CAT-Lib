package dev.astler.cat_ui.fragments

import android.os.Bundle
import android.view.View
import dev.astler.cat_ui.interfaces.RecyclerAdapterSizeListener
import dev.astler.cat_ui.utils.views.hideFABOnScroll
import dev.astler.cat_ui.utils.views.setBottomMarginInsets
import dev.astler.cat_ui.utils.views.showViewWithCondition
import dev.astler.cat_ui.views.CatStateLayout
import dev.astler.catlib.ui.databinding.RecyclerViewFragmentBinding

enum class InsetsPattern {
    SYSTEM_WITH_ACTION_BAR, SYSTEM, TOP, TOP_WITH_ACTION_BAR, BOTTOM, DISMISS
}

abstract class CatListFragment : CatFragment<RecyclerViewFragmentBinding>(RecyclerViewFragmentBinding::inflate),
    RecyclerAdapterSizeListener {

    protected open var showFloatingActionButton = false
    protected open var insetsPattern = InsetsPattern.BOTTOM

    override fun setLoadedItems(size: Int) {
        binding.stateLayout.activeView = if (size <= 0) {
            CatStateLayout.errorView
        } else {
            CatStateLayout.defaultView
        }
    }

    open fun onFABClick() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            stateLayout.activeView = CatStateLayout.loadingView

            fab.showViewWithCondition(showFloatingActionButton)

            if (showFloatingActionButton)  {
                recyclerView.hideFABOnScroll(fab)
                fab.setOnClickListener { onFABClick() }
                fab.setBottomMarginInsets()
            }

            applyPaddingPattern(insetsPattern, recyclerView)
        }
    }
}
