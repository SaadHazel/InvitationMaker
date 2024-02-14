package com.saad.invitationmaker.features.editor.callbacks

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

    fun updateZOrder()
}