package com.saad.invitationmaker.features.backgrounds.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.core.extensions.loadFromUrl
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.databinding.BackgroundChildRecyclerViewItemBinding
import com.saad.invitationmaker.features.backgrounds.callbacks.BackgroundCallBack

class BackgroundChildAdapter(
    private val dataList: List<Hit>,
    private val onItemClick: BackgroundCallBack,
) :
    RecyclerView.Adapter<BackgroundChildAdapter.ChildViewHolder>() {

    inner class ChildViewHolder(private val binding: BackgroundChildRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Hit, onItemClick: BackgroundCallBack) {
            binding.singleCardImg.loadFromUrl(data.largeImageURL)
            itemView.setOnClickListener {
                onItemClick.onBackgroundClick(data.largeImageURL)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding = BackgroundChildRecyclerViewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data, onItemClick)
    }

    override fun getItemCount(): Int = dataList.size
}