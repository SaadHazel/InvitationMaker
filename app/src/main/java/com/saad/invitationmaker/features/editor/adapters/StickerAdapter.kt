package com.saad.invitationmaker.features.editor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.databinding.ItemStickerBinding
import com.saad.invitationmaker.features.editor.callbacks.StickerCallBack

class StickerAdapter(
    private val images: List<String>,
    private val stickerClickListener: StickerCallBack,
) :
    RecyclerView.Adapter<StickerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = images[position]
        holder.bind(imageUrl, stickerClickListener)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(private val binding: ItemStickerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String, stickerClickListener: StickerCallBack) {
            Utils.log("Image Url: $imageUrl")
            // Using Glide to load image into ImageView
            itemView.setOnClickListener {
                stickerClickListener.onStickerClick(imageUrl)
            }
            Glide.with(binding.imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.baseline_star_24)
                .into(binding.imageView)
        }
    }
}
