package dev.astler.unli.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import dev.astler.unli.adapters.viewholders.BaseOneItemListViewHolder
import dev.astler.unli.interfaces.RecyclerAdapterSizeListener

open class BaseOneItemListAdapter<T>(@LayoutRes val pLayoutResource: Int, private val mItemLoadListener: LoadItem<T>? = null, private val mAdapterSizeListener: RecyclerAdapterSizeListener? = null) : RecyclerView.Adapter<BaseOneItemListViewHolder>() {

    var data: ArrayList<T> = arrayListOf()

    fun addItems(items: List<T>) {
        this.data.clear()
        this.data.addAll(items)
        mAdapterSizeListener?.totalItems(this.data.size)
        notifyDataSetChanged()
    }

    fun silenceReloadItems(items: List<T>) {
        this.data.clear()
        this.data.addAll(items)
        mAdapterSizeListener?.totalItems(this.data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseOneItemListViewHolder {
        val nHolderItem = LayoutInflater.from(parent.context).inflate(pLayoutResource, parent, false)
        return BaseOneItemListViewHolder(nHolderItem)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseOneItemListViewHolder, position: Int) {
        mItemLoadListener?.loadData(data[position], holder)
    }

    fun interface LoadItem<T> {
        fun loadData(pData: T, pHolder: BaseOneItemListViewHolder)
    }
}
