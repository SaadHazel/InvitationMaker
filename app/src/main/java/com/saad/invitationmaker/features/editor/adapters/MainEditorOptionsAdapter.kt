package com.saad.invitationmaker.features.editor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.databinding.MainEditorOptionsItemBinding
import com.saad.invitationmaker.features.editor.callbacks.MainEditorOptionsItemClick
import com.saad.invitationmaker.features.editor.models.MainOptionsData

class MainOptionsAdapter(
    private val dataList: List<MainOptionsData>,
    private val onItemClick: MainEditorOptionsItemClick,
) :
    RecyclerView.Adapter<MainOptionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainEditorOptionsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: MainEditorOptionsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MainOptionsData) {
            itemView.setOnClickListener {
                onItemClick.onItemClick(data.text)
            }
            binding.data = data
            binding.executePendingBindings()
        }
    }
}
