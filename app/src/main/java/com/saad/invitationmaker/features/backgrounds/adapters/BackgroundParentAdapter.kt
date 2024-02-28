package com.saad.invitationmaker.features.backgrounds.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.databinding.BackgroundParentRecyclerViewItemBinding
import com.saad.invitationmaker.features.backgrounds.callbacks.BackgroundCallBack
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel

class BackgroundParentAdapter(
    private val dataList: List<CategoryModel>,
    private val onItemClick: BackgroundCallBack,
) :
    RecyclerView.Adapter<BackgroundParentAdapter.ParentViewHolder>() {

    inner class ParentViewHolder(private val binding: BackgroundParentRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CategoryModel, onItemClick: BackgroundCallBack) {

            binding.headingText.text = data.category
            binding.childRecyclerView.layoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.seeAllCategory.setOnClickListener {
                onItemClick.onCategoryClick(dataList)
            }

            val childAdapter =
                BackgroundChildAdapter(data.hit, object : BackgroundCallBack {
                    override fun onBackgroundClick(url: String) {
                        onItemClick.onBackgroundClick(url)
                    }

                    override fun onCategoryClick(data: List<CategoryModel>) {
                        //Nothing for now
                    }
                })
            binding.childRecyclerView.adapter = childAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BackgroundParentRecyclerViewItemBinding.inflate(inflater, parent, false)
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data, onItemClick)
    }

    override fun getItemCount(): Int = dataList.size
}