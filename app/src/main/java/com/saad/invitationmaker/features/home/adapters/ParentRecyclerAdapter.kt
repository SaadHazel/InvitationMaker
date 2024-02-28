package com.saad.invitationmaker.features.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.databinding.HomeHorizontalRecyclerViewItemBinding
import com.saad.invitationmaker.features.home.callbacks.SingleDesignCallBack
import com.saad.invitationmaker.features.home.models.MainCardModel

class ParentRecyclerAdapter(
    private val dataList: List<MainCardModel>,
    private val onItemClick: SingleDesignCallBack,
) :
    RecyclerView.Adapter<ParentRecyclerAdapter.ParentViewHolder>() {

    inner class ParentViewHolder(private val binding: HomeHorizontalRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MainCardModel, onItemClick: SingleDesignCallBack) {
            binding.imgIcon.setImageResource(data.imageIcon)
            binding.headingText.text = data.heading


            binding.childRecyclerView.layoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            val childAdapter =
                ChildRecyclerViewAdapter(data.allDesigns, object : SingleDesignCallBack {
                    override fun onDesignClick(category: String, docId: String) {
                        onItemClick.onDesignClick(category, docId)
                    }

                })
            binding.childRecyclerView.adapter = childAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeHorizontalRecyclerViewItemBinding.inflate(inflater, parent, false)
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data, onItemClick)
    }

    override fun getItemCount(): Int = dataList.size
}