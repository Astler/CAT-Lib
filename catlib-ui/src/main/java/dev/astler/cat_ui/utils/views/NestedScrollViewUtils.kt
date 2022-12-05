package dev.astler.cat_ui.utils.views

import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun NestedScrollView.hideFABOnScroll(fab: FloatingActionButton) {
    setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
        if (scrollY > oldScrollY) {
            fab.hide()
        } else {
            fab.show()
        }
    }
}
