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
import dev.astler.unlib.utils.hideFABOnScroll
import dev.astler.unlib.view.StateLayout

abstract class UnLibSimpleListFragment :
    UnLibFragment(R.layout.recycler_view_fragment),
    RecyclerAdapterSizeListener {

    protected lateinit var mRecyclerViewFragmentBinding: RecyclerViewFragmentBinding
    lateinit var mStateLayout: StateLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mFABView: FloatingActionButton
    protected open var mFABVisible = false

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

            mFABView.setOnClickListener { _ ->
                onFABClick()
            }

            mRecyclerView.hideFABOnScroll(mFABView)
        } else {
            mFABView.visibility = View.GONE
        }

        setHasOptionsMenu(true)

        return mRecyclerViewFragmentBinding.root
    }
}
