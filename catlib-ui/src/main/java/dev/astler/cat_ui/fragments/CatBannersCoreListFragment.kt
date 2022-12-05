package dev.astler.cat_ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.databinding.FragmentListToolbarBinding
import dev.astler.cat_ui.interfaces.RecyclerAdapterSizeListener
import dev.astler.cat_ui.utils.setStatusBarColor
import dev.astler.cat_ui.utils.views.showViewWithCondition
import dev.astler.cat_ui.views.CatStateLayout

@AndroidEntryPoint
abstract class CatBannersCoreListFragment : CatFragment<FragmentListToolbarBinding>(),
    RecyclerAdapterSizeListener {

    protected var bannerTag: String = this::class.java.name

    private var _recyclerView: RecyclerView? = null

    open var showFloatingActionButton: Boolean = false
    open var showToolbar: Boolean = true
    open var liftToolbarOnScroll: Boolean = false
    open var allowOverrideTitle: Boolean = false
    open var titleId: Int = -1

    lateinit var mStateLayout: CatStateLayout

    override fun setLoadedItems(size: Int) {
        if (size <= 0) {
            mStateLayout.activeView = CatStateLayout.errorView
        } else {
            mStateLayout.activeView = CatStateLayout.defaultView
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentListToolbarBinding
        get() = FragmentListToolbarBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setStatusBarColor(android.R.color.transparent)

        with(binding) {

            mStateLayout = stateLayout
            _recyclerView = recyclerView

            mStateLayout.activeView = CatStateLayout.loadingView

            toolbarLayout.customTopBar.showViewWithCondition(showToolbar)
            if (showToolbar) coreListener?.setupToolbar(toolbarLayout.toolbar)

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

    override fun onResume() {
        super.onResume()

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