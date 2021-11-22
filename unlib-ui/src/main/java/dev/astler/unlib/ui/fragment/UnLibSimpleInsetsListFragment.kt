package dev.astler.unlib.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.astler.unlib.interfaces.RecyclerAdapterSizeListener
import dev.astler.unlib.ui.R
import dev.astler.unlib.ui.databinding.RecyclerViewFragmentBinding
import dev.astler.unlib.utils.canShowAds
import dev.astler.unlib.utils.views.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.view.StateLayout

enum class ListInsetsType {
    SYSTEM_WITH_ACTION_BAR, SYSTEM, TOP, TOP_WITH_ACTION_BAR, BOTTOM, DISMISS
}

abstract class UnLibSimpleInsetsListFragment :
    UnLibFragment(R.layout.recycler_view_fragment),
    RecyclerAdapterSizeListener {

    private lateinit var mRecyclerViewFragmentBinding: RecyclerViewFragmentBinding
    lateinit var mStateLayout: StateLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mFABView: FloatingActionButton
    protected open var mFABVisible = false
    protected open var mWithBottomAds = false
    protected open var mListInsetsType = ListInsetsType.BOTTOM

    override fun totalItems(size: Int) {
        if (size <= 0) {
            mStateLayout.activeView = StateLayout.errorView
        } else {
            mStateLayout.activeView = StateLayout.defaultView
        }
    }

    open fun onFABClick() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val nView = super.onCreateView(inflater, container, savedInstanceState)

        mRecyclerViewFragmentBinding = if (nView == null) RecyclerViewFragmentBinding.inflate(inflater, container, false)
        else RecyclerViewFragmentBinding.bind(nView)

        mStateLayout = mRecyclerViewFragmentBinding.stateLayout
        mRecyclerView = mRecyclerViewFragmentBinding.recyclerView
        mFABView = mRecyclerViewFragmentBinding.fab

        mStateLayout.activeView = StateLayout.loadingView

        if (mFABVisible) {
            mFABView.visibility = View.VISIBLE

            mFABView.setOnClickListener { onFABClick() }

            mRecyclerView.hideFABOnScroll(mFABView)
        } else {
            mFABView.visibility = View.GONE
        }

        setHasOptionsMenu(true)

        mFABView.setBottomMarginInsets()

        when (mListInsetsType) {
            ListInsetsType.SYSTEM_WITH_ACTION_BAR -> {
                val nAddBottomPadding = !mWithBottomAds || !requireContext().canShowAds()

                if (nAddBottomPadding)
                    mRecyclerView.setStatusAndNavigationPaddingForView(pAdditionalTopPadding = resources.getDimensionPixelSize(R.dimen.toolbar_height))
                else mRecyclerView.setStatusPaddingForView(pAdditionalTopPadding = resources.getDimensionPixelSize(R.dimen.toolbar_height))
            }
            ListInsetsType.SYSTEM -> {
                val nAddBottomPadding = !mWithBottomAds || !requireContext().canShowAds()

                if (nAddBottomPadding)
                    mRecyclerView.setStatusAndNavigationPaddingForView()
                else mRecyclerView.setStatusPaddingForView()
            }
            ListInsetsType.BOTTOM -> {
                val nAddBottomPadding = !mWithBottomAds || !requireContext().canShowAds()

                if (nAddBottomPadding)
                    mRecyclerView.setNavigationPaddingForView()
            }
            ListInsetsType.TOP -> {
                mRecyclerView.setStatusPaddingForView()
            }
            ListInsetsType.TOP_WITH_ACTION_BAR -> {
                mRecyclerView.setStatusPaddingForView(pAdditionalTopPadding = resources.getDimensionPixelSize(R.dimen.toolbar_height))
            }
            else -> {}
        }

        return mRecyclerViewFragmentBinding.root
    }
}
