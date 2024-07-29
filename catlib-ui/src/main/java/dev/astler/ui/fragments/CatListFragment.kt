package dev.astler.ui.fragments

import android.os.Bundle
import android.view.View
import dev.astler.ui.interfaces.RecyclerAdapterSizeListener
import dev.astler.ui.utils.views.hideFABOnScroll
import dev.astler.ui.utils.views.setBottomMarginInsets
import dev.astler.ui.utils.views.showWithCondition
import dev.astler.ui.views.CatStateLayout
import dev.astler.ui.databinding.RecyclerViewFragmentBinding

abstract class CatListFragment : CatFragment<RecyclerViewFragmentBinding>(RecyclerViewFragmentBinding::inflate),
    RecyclerAdapterSizeListener {

    protected open var showFloatingActionButton = false

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

            fab.showWithCondition(showFloatingActionButton)

            if (showFloatingActionButton)  {
                recyclerView.hideFABOnScroll(fab)
                fab.setOnClickListener { onFABClick() }
                fab.setBottomMarginInsets()
            }
        }
    }
}
