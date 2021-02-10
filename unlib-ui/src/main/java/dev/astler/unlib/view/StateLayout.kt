package dev.astler.unlib.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import dev.astler.unlib.R

//based on https://github.com/wangshouquan/StateLayout

class StateLayout @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val errorView = -1
        const val loadingView = 0
        const val defaultView = 1
    }

    var activeView = defaultView
        set(value) {
            if (this.activeView == value) {
                return
            }
            if (!viewsMap.containsKey(value)) {
                throw IllegalStateException("Invalid view id: $value")
            }
            for (key in viewsMap.keys) {
                viewsMap[key]!!.visibility = if (key == value) View.VISIBLE else View.GONE
            }

            field = value
        }

    private var viewsMap = HashMap<Int, View>()

    private var loadingResId = R.layout.loading_view
    private var errorResId = R.layout.empty_view

    init {
        val attrsStateLayout = context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        loadingResId = attrsStateLayout.getResourceId(R.styleable.StateLayout_unli_loading_view, R.layout.loading_view)
        errorResId = attrsStateLayout.getResourceId(R.styleable.StateLayout_unli_error_view, R.layout.empty_view)
        attrsStateLayout.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (childCount > 1) {
            throw IllegalArgumentException("You must have only one content view.")
        }
        if (childCount == 1) {
            val contentView = getChildAt(0)
            viewsMap[defaultView] = contentView
        }

        if (loadingResId != -1) {
            setViewForState(loadingView, loadingResId)
        }

        if (errorResId != -1) {
            setViewForState(errorView, errorResId)
        }
    }

    private fun setViewForState(viewTypeId: Int, @LayoutRes resId: Int) {
        val view = LayoutInflater.from(context).inflate(resId, this, false)
        setViewForState(viewTypeId, view)
    }

    private fun setViewForState(state: Int, view: View) {
        if (viewsMap.containsKey(state)) {
            removeView(viewsMap[state])
        }

        addView(view)
        view.visibility = View.GONE

        viewsMap[state] = view
    }

    fun getView(state: Int): View? {
        return viewsMap[state]
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return if (superState == null) {
            superState
        } else {
            SavedState(superState, activeView)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            activeView = state.state
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    internal class SavedState : BaseSavedState {

        var state: Int

        constructor(superState: Parcelable, state: Int) : super(superState) {
            this.state = state
        }

        constructor(source: Parcel) : super(source) {
            state = source.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(state)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }


}