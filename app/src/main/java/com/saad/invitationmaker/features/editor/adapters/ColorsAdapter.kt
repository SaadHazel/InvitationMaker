package com.saad.invitationmaker.features.editor.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.R
import com.saad.invitationmaker.databinding.ColorLayoutSingleViewBinding
import com.saad.invitationmaker.features.editor.callbacks.ItemColorCallBack

class ColorsAdapter(
    private val list: List<String>,
    private val itemClickListener: ItemColorCallBack,
) :
    RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
        val binding =
            ColorLayoutSingleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
        holder.bind(list[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ColorsViewHolder(private val binding: ColorLayoutSingleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: String, itemClickListener: ItemColorCallBack) {
            val roundedDrawable = GradientDrawable()
            roundedDrawable.shape = GradientDrawable.RECTANGLE
            roundedDrawable.cornerRadius = 30f
            roundedDrawable.setColor(Color.parseColor(color))

            if (position == 0) {
                itemView.setBackgroundResource(R.drawable.color_picker_icon)
            } else {
                binding.colorView.background = roundedDrawable
            }

            itemView.setOnClickListener {

                if (position == 0) {
                    itemClickListener.onItemColorPickerClick(true)
                } else {
                    itemClickListener.onItemColorPickerClick(false)
                }
                itemClickListener.onItemColorClick(color)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Return a unique view type for each position
        return position
    }

}