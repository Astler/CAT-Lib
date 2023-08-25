package dev.astler.cat_ui.utils.views

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun visible(vararg viewsToShow: View) {
    viewsToShow.forEach {
        it.visible()
    }
}

fun gone(vararg viewsToHide: View) {
    viewsToHide.forEach {
        it.gone()
    }
}

fun View.showViewWithCondition(condition: Boolean) {
    if (condition) {
        visible()
    } else {
        gone()
    }
}
