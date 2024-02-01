package com.saad.invitationmaker.features.home.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.app.models.TabData
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.databinding.HorizontalCategoryItemBinding
import com.saad.invitationmaker.features.home.callbacks.HorizontalCategoryItemClick

class HorizontalCategoryAdapter(
    private val itemList: List<TabData>,
    private val itemClickListener: HorizontalCategoryItemClick,
) : RecyclerView.Adapter<HorizontalCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HorizontalCategoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(private val binding: HorizontalCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TabData, itemClickListener: HorizontalCategoryItemClick) {
            binding.data = item
            binding.itemClick = itemClickListener
            binding.whiteView.visibility = View.VISIBLE
            binding.text1.setTextColor(Color.BLACK)

            itemView.setOnClickListener {
                Utils.log("Clicked on item View")
                binding.whiteView.visibility = View.GONE
                binding.text1.setTextColor(Color.WHITE)
            }

            // Executing binding immediately to refresh UI
            binding.executePendingBindings()
        }
    }
}