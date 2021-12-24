package dev.astler.cat_ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.astler.cat_ui.adapters.diffutils.UniversalDiffUtil
import dev.astler.cat_ui.adapters.viewholders.CatOneTypeViewHolder
import dev.astler.cat_ui.interfaces.RecyclerAdapterSizeListener

open class CatOneTypeAdapter<T>(@LayoutRes val pLayoutResource: Int, private val mItemLoadListener: LoadItem<T>? = null, private val mAdapterSizeListener: RecyclerAdapterSizeListener? = null) : RecyclerView.Adapter<CatOneTypeViewHolder>() {

    var data: ArrayList<T> = arrayListOf()

    fun setData(items: List<T>) {

        val callback = UniversalDiffUtil(data, items)
        val productDiffResult = DiffUtil.calculateDiff(callback)

        this.data.clear()
        this.data.addAll(items)
        mAdapterSizeListener?.setLoadedItems(this.data.size)

        productDiffResult.dispatchUpdatesTo(this)
    }

    fun setDataSilence(items: List<T>) {
        this.data.clear()
        this.data.addAll(items)
        mAdapterSizeListener?.setLoadedItems(this.data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatOneTypeViewHolder {
        val nHolderItem = LayoutInflater.from(parent.context).inflate(pLayoutResource, parent, false)
        return CatOneTypeViewHolder(nHolderItem)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CatOneTypeViewHolder, position: Int) {
        mItemLoadListener?.loadData(data[position], holder)
    }

    fun interface LoadItem<T> {
        fun loadData(pData: T, pHolder: CatOneTypeViewHolder)
    }
}
