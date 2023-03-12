package dev.astler.cat_ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.astler.cat_ui.views.CatStateLayout
import dev.astler.cat_ui.interfaces.RecyclerAdapterSizeListener
import dev.astler.cat_ui.utils.views.*
import dev.astler.catlib.utils.canShowAds
import dev.astler.catlib.ui.databinding.RecyclerViewFragmentBinding

enum class ListInsetsType {
    SYSTEM_WITH_ACTION_BAR, SYSTEM, TOP, TOP_WITH_ACTION_BAR, BOTTOM, DISMISS
}

abstract class CatListFragment : CatFragment<RecyclerViewFragmentBinding>(), RecyclerAdapterSizeListener {

    private lateinit var mRecyclerViewFragmentBinding: RecyclerViewFragmentBinding
    lateinit var mStateLayout: CatStateLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mFABView: FloatingActionButton
    protected open var mFABVisible = false
    protected open var mWithBottomAds = false
    protected open var mListInsetsType = ListInsetsType.BOTTOM

    override fun setLoadedItems(size: Int) {
        if (size <= 0) {
            mStateLayout.activeView = CatStateLayout.errorView
        } else {
            mStateLayout.activeView = CatStateLayout.defaultView
        }
    }

    open fun onFABClick() {}

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> RecyclerViewFragmentBinding
        get() = RecyclerViewFragmentBinding::inflate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val nView = super.onCreateView(inflater, container, savedInstanceState)

        mRecyclerViewFragmentBinding =
            if (nView == null) RecyclerViewFragmentBinding.inflate(inflater, container, false)
            else RecyclerViewFragmentBinding.bind(nView)

        mStateLayout = mRecyclerViewFragmentBinding.stateLayout
        mRecyclerView = mRecyclerViewFragmentBinding.recyclerView
        mFABView = mRecyclerViewFragmentBinding.fab

        mStateLayout.activeView = CatStateLayout.loadingView

        if (mFABVisible) {
            mFABView.visibility = View.VISIBLE

            mFABView.setOnClickListener { onFABClick() }

            mRecyclerView.hideFABOnScroll(mFABView)
        } else {
            mFABView.visibility = View.GONE
        }

        mFABView.setBottomMarginInsets()

        when (mListInsetsType) {
            ListInsetsType.SYSTEM_WITH_ACTION_BAR -> {
                val nAddBottomPadding = !(mWithBottomAds && requireContext().canShowAds())

                if (nAddBottomPadding)
                    mRecyclerView.setStatusAndNavigationPaddingForView(
                        pAdditionalTopPadding = coreListener?.getToolbarHeight() ?: 0
                    )
                else mRecyclerView.setStatusPaddingForView(
                    pAdditionalTopPadding = coreListener?.getToolbarHeight() ?: 0
                )
            }
            ListInsetsType.SYSTEM -> {
                val nAddBottomPadding = !(mWithBottomAds && requireContext().canShowAds())

                if (nAddBottomPadding)
                    mRecyclerView.setStatusAndNavigationPaddingForView()
                else mRecyclerView.setStatusPaddingForView()
            }
            ListInsetsType.BOTTOM -> {
                val nAddBottomPadding = !(mWithBottomAds && requireContext().canShowAds())

                if (nAddBottomPadding)
                    mRecyclerView.setNavigationPaddingForView()
            }
            ListInsetsType.TOP -> {
                mRecyclerView.setStatusPaddingForView()
            }
            ListInsetsType.TOP_WITH_ACTION_BAR -> {
                mRecyclerView.setStatusPaddingForView(
                    pAdditionalTopPadding = coreListener?.getToolbarHeight() ?: 0
                )
            }
            else -> {}
        }

        return mRecyclerViewFragmentBinding.root
    }
}
