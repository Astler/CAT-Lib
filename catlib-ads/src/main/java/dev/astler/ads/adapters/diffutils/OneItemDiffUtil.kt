package dev.astler.ads.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil

class OneItemDiffUtil<T>(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {

    override fun getOldListSize() =  oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct: T = oldList[oldItemPosition]
        val newProduct: T = newList[newItemPosition]
        return oldProduct.hashCode() == newProduct.hashCode()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct: T = oldList[oldItemPosition]
        val newProduct: T = newList[newItemPosition]
        return oldProduct?.equals(newProduct) == true
    }
}
