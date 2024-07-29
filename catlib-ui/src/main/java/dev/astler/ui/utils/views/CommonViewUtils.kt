package dev.astler.ui.utils.views

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.show() {
    visible()
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    gone()
}

fun visible(vararg viewsToShow: View) {
    viewsToShow.forEach {
        it.visible()
    }
}

fun show(vararg viewsToShow: View) {
    visible(*viewsToShow)
}

fun gone(vararg viewsToHide: View) {
    viewsToHide.forEach {
        it.gone()
    }
}

fun hide(vararg viewsToHide: View) {
    gone(*viewsToHide)
}

fun View.visibleWithCondition(condition: Boolean): Boolean {
    if (condition) {
        visible()
    } else {
        gone()
    }
    return condition
}

fun View.showWithCondition(condition: Boolean): Boolean {
    visibleWithCondition(condition)
    return condition
}

fun visibleWithCondition(condition: Boolean, vararg views: View): Boolean {
    views.forEach {
        it.showWithCondition(condition)
    }

    return condition
}

fun showWithCondition(condition: Boolean, vararg views: View): Boolean {
    visibleWithCondition(condition, *views)
    return condition
}
