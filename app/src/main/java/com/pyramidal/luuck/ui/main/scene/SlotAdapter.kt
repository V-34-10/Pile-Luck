package com.pyramidal.luuck.ui.main.scene

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pyramidal.luuck.R
import com.pyramidal.luuck.ui.main.scene.model.SlotItem
import com.pyramidal.luuck.ui.utils.SlotItemDiffCallback

class SlotAdapter (private var dataList: List<SlotItem>) :
    RecyclerView.Adapter<SlotAdapter.SlotViewHolder>() {
    private var previousDataList: List<SlotItem> = emptyList()
    inner class SlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slotImageView: ImageView = itemView.findViewById(R.id.imageSlot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slot, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slotItem = dataList[position]
        holder.slotImageView.setImageResource(slotItem.imageResId)
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slot_animation)
        holder.itemView.startAnimation(animation)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<SlotItem>) {
        // Зберігаємо попередній список даних
        previousDataList = dataList.toList()

        // Оновлюємо дані
        dataList = newData

        // Виконуємо анімацію зміни даних
        val diffCallback = SlotItemDiffCallback(previousDataList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    fun playSpinAnimation(recyclerView: RecyclerView, context: Context) {
        val itemCount = itemCount
        for (i in 0 until itemCount) {
            val holder = recyclerView.findViewHolderForAdapterPosition(i) as? SlotViewHolder
            holder?.let {
                val animation = AnimationUtils.loadAnimation(context, R.anim.slot_animation)
                it.itemView.startAnimation(animation)
            }
        }
    }
}