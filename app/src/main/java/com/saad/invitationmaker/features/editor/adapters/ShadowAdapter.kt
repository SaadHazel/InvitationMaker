package com.saad.invitationmaker.features.editor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.databinding.ShadowLayoutSingleItemBinding
import com.saad.invitationmaker.features.editor.callbacks.ItemShadowClickCallback

class ShadowAdapter(
    private val shadowOptionList: List<String>,
    private val itemClickListener: ItemShadowClickCallback,
) : RecyclerView.Adapter<ShadowAdapter.ShadowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShadowViewHolder {
        val binding = ShadowLayoutSingleItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShadowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShadowViewHolder, position: Int) {
        val shadowOption = shadowOptionList[position]
        holder.bind(shadowOption)
    }

    override fun getItemCount(): Int {
        return shadowOptionList.size
    }

    inner class ShadowViewHolder(private val binding: ShadowLayoutSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shadowOption: String) {
            binding.shadowItem.text = shadowOption
            binding.root.setOnClickListener {
                itemClickListener.onShadowClick(shadowOption)
            }
        }
    }
}
