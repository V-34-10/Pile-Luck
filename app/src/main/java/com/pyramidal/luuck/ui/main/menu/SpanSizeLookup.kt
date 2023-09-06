package com.pyramidal.luuck.ui.main.menu

import androidx.recyclerview.widget.GridLayoutManager

class SpanSizeLookup (private val adapter: RecyclerAdapter) : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return when (adapter.getItemViewType(position)) {
            RecyclerAdapter.VIEW_TYPE_LARGE -> 2 // Один великий елемент на стовпець
            RecyclerAdapter.VIEW_TYPE_SMALL -> 1 // Два маленькі елементи на стовпець
            else -> 1
        }
    }
}