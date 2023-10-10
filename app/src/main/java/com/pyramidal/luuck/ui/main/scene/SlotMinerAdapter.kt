package com.pyramidal.luuck.ui.main.scene

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pyramidal.luuck.R
import com.pyramidal.luuck.ui.main.scene.games.GameFifeFragment
import com.pyramidal.luuck.ui.main.scene.model.SlotItem
import java.util.Random

class SlotMinerAdapter(private var dataList: List<SlotItem>) :
    RecyclerView.Adapter<SlotMinerAdapter.SlotViewHolder>() {
    private var slotListGame = mutableListOf(
        R.drawable.slot_006,
        R.drawable.slot_001,
        R.drawable.slot_003,
        R.drawable.slot_004,
        R.drawable.slot_006,
        R.drawable.slot_001,
        R.drawable.slot_004,
        R.drawable.slot_002,
        R.drawable.slot_006
    )
    private var itemClickListener: SlotItemClickListener? = null
    private var openSlotCount = 0
    private var maxOpenSlotCount = 3

    inner class SlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slotImageView: ImageView = itemView.findViewById(R.id.imageSlot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_miner_slot, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slotItem = dataList[position]
        holder.slotImageView.setImageResource(slotItem.imageResId)
        holder.itemView.setOnClickListener {
            if (openSlotCount < maxOpenSlotCount) {
                val randomImageIndex = generateRandomImageIndex()
                slotListGame.shuffle()
                val randomImageResId = slotListGame.map { SlotItem(it) }[randomImageIndex]

                holder.slotImageView.setImageResource(randomImageResId.imageResId)
                itemClickListener?.onItemClick(position, randomImageResId)
                openSlotCount++
            }
        }
    }

    fun setItemClickListener(listener: GameFifeFragment) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<SlotItem>) {
        dataList = newData
        notifyDataSetChanged()
        openSlotCount = 0
    }

    private fun generateRandomImageIndex(): Int {
        val random = Random()
        return random.nextInt(dataList.size)
    }
}