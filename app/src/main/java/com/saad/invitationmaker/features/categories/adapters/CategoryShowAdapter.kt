package com.saad.invitationmaker.features.categories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saad.invitationmaker.R
import com.saad.invitationmaker.databinding.ItemImageBinding
import com.saad.invitationmaker.features.home.models.AllCardsDesigns


class CategoryShowAdapter(
    private var itemList: List<String>? = null,
    private var homeDataList: List<AllCardsDesigns>? = null,
    private val onItemClick: (String, String?) -> Unit,
) : RecyclerView.Adapter<CategoryShowAdapter.ImageViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (itemList != null) {
            val document = itemList?.get(position)
            if (document != null) {
                holder.bind(design = document)
            }
        } else {
            val document = homeDataList?.get(position)
            if (document != null) {
                holder.bind(homeData = document)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (itemList != null)
            itemList!!.size
        else
            homeDataList!!.size
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(design: String? = null, homeData: AllCardsDesigns? = null) {
            binding.imageView1.setOnClickListener {
                if (design != null) {
                    onItemClick(design, "")
                } else {
                    if (homeData != null) {
                        onItemClick(homeData.category, homeData.docId)
                    }
                }
            }
            if (design != null) {
                Glide.with(itemView.context)
                    .load(design)
                    .centerInside()
                    .placeholder(R.drawable.baseline_search_24)
                    .into(binding.imageView1)
            } else {
                if (homeData != null) {
                    Glide.with(itemView.context)
                        .load(homeData.thumbnail)
                        .centerInside()
                        .placeholder(R.drawable.baseline_search_24)
                        .into(binding.imageView1)
                }
            }
        }
    }


}
