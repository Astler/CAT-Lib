package dev.astler.unli.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.astler.unli.R
import dev.astler.unli.hideFABOnScroll
import dev.astler.unli.interfaces.RecyclerAdapterSizeListener
import dev.astler.unli.view.StateLayout
import kotlinx.android.synthetic.main.recycler_view_fragment.view.*

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

    open fun onFABClick() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.recycler_view_fragment, container, false)

        mStateLayout = view.stateLayout
        mRecyclerView = view.recyclerView
        mFABView = view.fab

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

        return view
    }
}