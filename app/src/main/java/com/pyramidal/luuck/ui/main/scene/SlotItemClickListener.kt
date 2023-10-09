package com.pyramidal.luuck.ui.main.scene

import com.pyramidal.luuck.ui.main.scene.model.SlotItem

interface SlotItemClickListener {
    fun onItemClick(position: Int, slotItem: SlotItem)
}