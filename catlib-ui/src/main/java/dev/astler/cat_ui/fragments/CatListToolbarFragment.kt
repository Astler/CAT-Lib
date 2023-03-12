package dev.astler.cat_ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.interfaces.RecyclerAdapterSizeListener
import dev.astler.cat_ui.utils.setStatusBarColor
import dev.astler.cat_ui.utils.views.showViewWithCondition
import dev.astler.cat_ui.views.CatStateLayout
import dev.astler.catlib.ui.databinding.FragmentListToolbarBinding

@AndroidEntryPoint
abstract class CatListToolbarFragment : CatFragment<FragmentListToolbarBinding>(),
    RecyclerAdapterSizeListener {

    private var _recyclerView: RecyclerView? = null

    open var showFloatingActionButton: Boolean = false
    open var showToolbar: Boolean = true
    open var liftToolbarOnScroll: Boolean = false
    open var allowOverrideTitle: Boolean = false
    open var titleId: Int = -1

    override fun setLoadedItems(size: Int) {
        binding.stateLayout.activeView =
            if (size <= 0) CatStateLayout.errorView else CatStateLayout.defaultView
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentListToolbarBinding
        get() = FragmentListToolbarBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setStatusBarColor(android.R.color.transparent)

        with(binding) {
            _recyclerView = recyclerView

            stateLayout.activeView = CatStateLayout.loadingView

            setupToolbar()

            fab.visibility = if (showFloatingActionButton) View.VISIBLE else View.GONE

            toolbarLayout.customTopBar.isLiftOnScroll = liftToolbarOnScroll

            ViewCompat.setOnApplyWindowInsetsListener(toolbarLayout.topContent) { v, insets ->
                val params = v.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = coreListener?.getTopPadding() ?: 0
                v.layoutParams = params
                insets
            }

            ViewCompat.setOnApplyWindowInsetsListener(fab) { v, insets ->
                val params = v.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = coreListener?.getBottomPadding() ?: 0
                v.layoutParams = params
                insets
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbarLayout.customTopBar.showViewWithCondition(showToolbar)

        if (showToolbar) coreListener?.setupToolbar(binding.toolbarLayout.toolbar)

        val title =
            (if (titleId <= 0) findNavController().currentDestination?.label else safeContext.getString(
                titleId
            ))
                ?: return

        if (allowOverrideTitle) {
            coreListener?.setToolbarTitle(title.toString())
        } else {
            binding.toolbarLayout.toolbar.title = title
        }
    }
}