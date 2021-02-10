package dev.astler.unli.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.astler.unli.interfaces.RecyclerAdapterSizeListener
import dev.astler.unlib.databinding.RecyclerViewFragmentBinding
import dev.astler.unlib.ui.fragments.CoreFragment
import dev.astler.unlib.utils.hideFABOnScroll
import dev.astler.unlib.view.StateLayout

abstract class BaseListFragment: CoreFragment(), RecyclerAdapterSizeListener {

    lateinit var mStateLayout: StateLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mFABView: FloatingActionButton
    protected open var mFABVisible = false

    override fun totalItems(size: Int) {
        if (size <= 0) {
            mStateLayout.activeView = StateLayout.errorView
        }
        else {
            mStateLayout.activeView = StateLayout.defaultView
        }
    }

    open fun onFABClick() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val nBinding = RecyclerViewFragmentBinding.inflate(inflater, container, false)

        mStateLayout = nBinding.stateLayout
        mRecyclerView = nBinding.recyclerView
        mFABView = nBinding.fab

        mStateLayout.activeView = StateLayout.loadingView

        if (mFABVisible) {
            mFABView.visibility = View.VISIBLE

            mFABView.setOnClickListener { _ ->
                onFABClick()
            }

            mRecyclerView.hideFABOnScroll(mFABView)
        }
        else {
            mFABView.visibility = View.GONE
        }

        setHasOptionsMenu(true)

        return nBinding.root
    }
}