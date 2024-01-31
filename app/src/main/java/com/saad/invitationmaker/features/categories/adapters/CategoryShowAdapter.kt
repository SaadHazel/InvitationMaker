package com.saad.invitationmaker.features.categories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saad.invitationmaker.R
import com.saad.invitationmaker.databinding.ItemImageBinding


class CategoryShowAdapter(
    private var itemList: MutableList<String>,
    private val onItemClick: (String) -> Unit,
) : RecyclerView.Adapter<CategoryShowAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val document = itemList[position]
        holder.bind(document)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(design: String) {
            binding.imageView1.setOnClickListener {
                onItemClick(design)
            }
            Glide.with(itemView.context)
                .load(design)
                .centerInside()
                .placeholder(R.drawable.baseline_search_24)
                .into(binding.imageView1)
        }
    }

    fun setData(itemList1: List<String>) {
        itemList.clear()
        itemList.addAll(itemList1)
        notifyDataSetChanged()

    }
}
