package com.saad.invitationmaker.features.editor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.core.extensions.loadFromUrl
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.databinding.BackgroundItemViewBinding
import com.saad.invitationmaker.features.backgrounds.callbacks.BackgroundCallBack

class BackgroundsAdapter(
    private val dataList: List<Hit>,
    private val onItemClick: BackgroundCallBack,
) :
    RecyclerView.Adapter<BackgroundsAdapter.BackgroundViewHolder>() {

    inner class BackgroundViewHolder(private val binding: BackgroundItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Hit, onItemClick: BackgroundCallBack) {
            binding.bdImg.loadFromUrl(data.largeImageURL)
            itemView.setOnClickListener {
                onItemClick.onBackgroundClick(data.largeImageURL)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundViewHolder {
        val binding = BackgroundItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BackgroundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BackgroundViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data, onItemClick)
    }

    override fun getItemCount(): Int = dataList.size
}