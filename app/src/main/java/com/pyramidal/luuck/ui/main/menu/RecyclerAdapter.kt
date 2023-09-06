package com.pyramidal.luuck.ui.main.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pyramidal.luuck.R

class RecyclerAdapter(private val dataList: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_SMALL = 1
        const val VIEW_TYPE_LARGE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SMALL -> SmallViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_small,
                    parent,
                    false
                )
            )
            VIEW_TYPE_LARGE -> LargeViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_large,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SmallViewHolder -> {
                val smallData = dataList[position] as SmallData
                holder.bind(smallData)
            }
            is LargeViewHolder -> {
                val largeData = dataList[position] as LargeData
                holder.bind(largeData)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataList[position]) {
            is SmallData -> VIEW_TYPE_SMALL
            is LargeData -> VIEW_TYPE_LARGE
            else -> throw IllegalArgumentException("Unknown data type at position $position")
        }
    }

    inner class SmallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(smallData: SmallData) {

        }
    }

    inner class LargeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(largeData: LargeData) {

        }
    }
}
