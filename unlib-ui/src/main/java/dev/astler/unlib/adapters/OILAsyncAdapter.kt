package dev.astler.unlib.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import dev.astler.unli.interfaces.RecyclerAdapterSizeListener
import dev.astler.unlib.view.AsyncCell

class OILAsyncAdapter<T> internal constructor(
    @LayoutRes val pLayoutResource: Int,
    private val mItemLoadListener: LoadOILItem<T>? = null,
    private val mAdapterSizeListener: RecyclerAdapterSizeListener? = null
) :
    RecyclerView.Adapter<OILAsyncAdapter.OILAsyncViewHolder>() {

    var mItems: ArrayList<T> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OILAsyncViewHolder =
        OILAsyncViewHolder(OILItemAsync(parent.context).apply { inflate() })

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: OILAsyncViewHolder, position: Int) {
        setUpOILItemAsyncViewHolder(holder, position)
    }

    private fun setUpOILItemAsyncViewHolder(pHolder: OILAsyncViewHolder, position: Int) {
        (pHolder.itemView as OILAsyncAdapter<*>.OILItemAsync).bindWhenInflated {
            mItems[position].let { pData ->
                mItemLoadListener?.loadData(pData, pHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = 0

    fun addItems(items: List<T>) {
        this.mItems.clear()
        this.mItems.addAll(items)
        mAdapterSizeListener?.totalItems(this.mItems.size)
        notifyDataSetChanged()
    }

    inner class OILItemAsync(context: Context) : AsyncCell(context) {

        override val layoutId = pLayoutResource

        override fun createDataBindingView(view: View): View {
            return view
        }
    }

    class OILAsyncViewHolder internal constructor(view: ViewGroup) :
        RecyclerView.ViewHolder(view)

}

interface LoadOILItem<T> {
    fun loadData(pData: T, pHolder: OILAsyncAdapter.OILAsyncViewHolder)
}