package dev.astler.unlib.utils.views

import android.view.View

fun View.showView() {
    visibility = View.VISIBLE
}

fun View.goneView() {
    visibility = View.GONE
}

fun showViews(vararg viewsToShow: View) {
    viewsToShow.forEach {
        it.showView()
    }
}

fun goneViews(vararg viewsToHide: View) {
    viewsToHide.forEach {
        it.goneView()
    }
}

fun View.showViewWithCondition(pCondition: Boolean) {
    if (pCondition) {
        showView()
    } else {
        goneView()
    }
}
