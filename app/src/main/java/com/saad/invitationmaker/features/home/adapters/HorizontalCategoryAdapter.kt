package com.saad.invitationmaker.features.home.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.app.models.TabData
import com.saad.invitationmaker.core.extensions.visible
import com.saad.invitationmaker.databinding.HorizontalCategoryItemBinding
import com.saad.invitationmaker.features.home.callbacks.HorizontalCategoryItemClick

class HorizontalCategoryAdapter(
    private val itemList: List<TabData>,
    private val itemClickListener: HorizontalCategoryItemClick,
    private val getPosition: (position: Int) -> Unit,
) : RecyclerView.Adapter<HorizontalCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HorizontalCategoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        getPosition(item.position)
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(private val binding: HorizontalCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TabData, itemClickListener: HorizontalCategoryItemClick) {
            binding.data = item
            itemView.setOnClickListener {
                itemView.isSelected = !itemView.isSelected
                if (itemView.isSelected) {
                    itemClickListener.itemClick(item)
                    binding.whiteView.visibility = View.INVISIBLE
                    binding.text1.setTextColor(Color.WHITE)
                    binding.text1.setTypeface(null, Typeface.BOLD)
                } else {
                    for (currentItem in itemList) {
                        val currentPosition = itemList.indexOf(currentItem)
                        if (currentPosition != adapterPosition) {
                            getPosition(currentPosition) // Notify the activity/fragment about the position
                            // Perform operations on the item view corresponding to currentPosition

                            binding.whiteView.visible()
                            binding.text1.setTextColor(Color.BLACK)
                            binding.text1.setTypeface(null, Typeface.NORMAL)
                        }
                    }
                }
            }
            // Executing binding immediately to refresh UI
            binding.executePendingBindings()
        }
    }
}