package dev.astler.ui.views

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.asynclayoutinflater.view.AsyncLayoutInflater

open class CatAsyncView(context: Context) : FrameLayout(context, null, 0) {

    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    open var layoutId = -1
    private var isInflated = false
    private val bindingFunctions: MutableList<CatAsyncView.() -> Unit> = mutableListOf()

    fun inflate() {
        AsyncLayoutInflater(context).inflate(layoutId, this) { view, _, _ ->
            isInflated = true
            addView(createDataBindingView(view))
            bindView()
        }
    }

    private fun bindView() {
        with(bindingFunctions) {
            forEach { it() }
            clear()
        }
    }

    fun bindWhenInflated(bindFunc: CatAsyncView.() -> Unit) {
        if (isInflated) {
            bindFunc()
        } else {
            bindingFunctions.add(bindFunc)
        }
    }

    open fun createDataBindingView(view: View): View? = view
}
