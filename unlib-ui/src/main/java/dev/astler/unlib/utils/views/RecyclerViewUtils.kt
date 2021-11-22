package dev.astler.unlib.utils.views

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.astler.unlib.view.MarginItemListDecorator

fun Context.getPossibleColumnsForScreenWight(imageWidth: Int = 50): Int {
    val displayMetrics = resources.displayMetrics
    val img = imageWidth / displayMetrics.density
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / img + 0.5).toInt()
}

fun RecyclerView.setupCardList(pAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(MarginItemListDecorator(22))
    adapter = pAdapter
    clipToPadding = false
}

fun RecyclerView.hideFABOnScroll(fab: FloatingActionButton) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0)
                fab.hide()
            else if (dy < 0)
                fab.show()
        }
    })
}
