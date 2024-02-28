package com.saad.invitationmaker.features.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.core.extensions.loadFromUrl
import com.saad.invitationmaker.databinding.HomeChildRecyclerViewItemBinding
import com.saad.invitationmaker.features.home.callbacks.SingleDesignCallBack
import com.saad.invitationmaker.features.home.models.AllCardsDesigns

class ChildRecyclerViewAdapter(
    private val dataList: List<AllCardsDesigns>,
    private val onItemClick: SingleDesignCallBack,
) :
    RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildViewHolder>() {

    inner class ChildViewHolder(private val binding: HomeChildRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AllCardsDesigns, onItemClick: SingleDesignCallBack) {
            binding.singleCardImg.loadFromUrl(data.thumbnail)
            itemView.setOnClickListener {
                onItemClick.onDesignClick(docId = data.docId, category = data.category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding = HomeChildRecyclerViewItemBinding.inflate(
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

