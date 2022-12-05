package dev.astler.cat_ui.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil

class UniversalDiffUtil<T>(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val nOldItem = oldList[oldItemPosition]
        val nNewItem = newList[newItemPosition]
        return nOldItem == nNewItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val nOldItem = oldList[oldItemPosition]
        val nNewItem = newList[newItemPosition]
        return nOldItem == nNewItem && nOldItem.hashCode() == nNewItem.hashCode()
    }
}
