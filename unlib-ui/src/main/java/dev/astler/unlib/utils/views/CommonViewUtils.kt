package dev.astler.unlib.utils.views

import android.view.View

fun View.showView() {
    visibility = View.VISIBLE
}

fun View.goneView() {
    visibility = View.GONE
}

fun View.showViewWithCondition(pCondition: Boolean) {
    if (pCondition) {
        showView()
    } else {
        goneView()
    }
}
