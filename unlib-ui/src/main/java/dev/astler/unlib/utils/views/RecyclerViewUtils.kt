package dev.astler.unlib.utils.views

import android.content.Context
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.astler.unlib.view.decorators.GridSpacingItemDecoration
import dev.astler.unlib.view.decorators.MarginHorizontalListDecorator
import dev.astler.unlib.view.decorators.MarginItemListDecorator

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

fun RecyclerView.setupNestedGrid(
    pAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
    itemWidth: Int,
    addMargin: Boolean = true,
    margin: Int = 8
) {
    ViewCompat.setNestedScrollingEnabled(this, false)

    val nItems = context.getPossibleColumnsForScreenWight(itemWidth) - 1

    val nGridManager = GridLayoutManager(context, nItems)

    if (addMargin)
        addItemDecoration(GridSpacingItemDecoration(nItems, margin, true))

    nGridManager.isItemPrefetchEnabled = true
    nGridManager.initialPrefetchItemCount = 10

    layoutManager = nGridManager
    setHasFixedSize(true)
    setItemViewCacheSize(20)

    adapter = pAdapter
}

fun RecyclerView.setupNestedList(
    pAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
    fixedSize: Boolean = true,
    addMargin: Boolean = true,
    margin: Int = 8
) {
    ViewCompat.setNestedScrollingEnabled(this, false)

    val nLinearLayoutManager = LinearLayoutManager(context)

    nLinearLayoutManager.isItemPrefetchEnabled = true
    nLinearLayoutManager.initialPrefetchItemCount = 5

    if (addMargin)
        addItemDecoration(MarginItemListDecorator(margin))

    setHasFixedSize(fixedSize)
    setItemViewCacheSize(20)

    layoutManager = nLinearLayoutManager
    adapter = pAdapter
}

fun RecyclerView.setupHorizontalScroll(
    pAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
    addMargin: Boolean = true,
    margin: Int = 8
) {
    ViewCompat.setNestedScrollingEnabled(this, false)

    val nLinearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    nLinearLayoutManager.isItemPrefetchEnabled = true
    nLinearLayoutManager.initialPrefetchItemCount = 5

    setHasFixedSize(true)
    setItemViewCacheSize(20)

    clipToPadding = false

    if (addMargin)
        addItemDecoration(MarginHorizontalListDecorator(margin))

    layoutManager = nLinearLayoutManager
    adapter = pAdapter
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
