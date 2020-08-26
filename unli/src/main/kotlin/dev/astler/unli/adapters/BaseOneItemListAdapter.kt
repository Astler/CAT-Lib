package dev.astler.unli.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import dev.astler.unli.adapters.viewholders.BaseOneItemListViewHolder

class BaseOneItemListAdapter<T>(array: ArrayList<T>, @LayoutRes val pLayoutResource: Int, private val mItemLoadListener: LoadItem<T>? = null) : RecyclerView.Adapter<BaseOneItemListViewHolder>() {

    var data = array

    fun addItems(items: List<T>) {
        this.data.clear()
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseOneItemListViewHolder {
        val nHolderItem = LayoutInflater.from(parent.context).inflate(pLayoutResource, parent, false)
        return BaseOneItemListViewHolder(nHolderItem)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseOneItemListViewHolder, position: Int) {
        mItemLoadListener?.loadData(data[position], holder.mItemView)
    }

    interface LoadItem<T> {
        fun loadData(pData: T, pItemView: View)
    }
}
