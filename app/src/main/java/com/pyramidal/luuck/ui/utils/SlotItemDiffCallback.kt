package com.pyramidal.luuck.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.pyramidal.luuck.ui.main.scene.model.SlotItem

class SlotItemDiffCallback(
    private val oldList: List<SlotItem>,
    private val newList: List<SlotItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].imageResId == newList[newItemPosition].imageResId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}