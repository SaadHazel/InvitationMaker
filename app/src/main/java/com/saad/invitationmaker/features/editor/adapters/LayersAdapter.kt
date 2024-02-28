package com.saad.invitationmaker.features.editor.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.R
import com.saad.invitationmaker.databinding.LayersItemBinding
import com.saad.invitationmaker.features.editor.callbacks.ItemTouchHelperAdapter
import com.saad.invitationmaker.features.editor.models.LayersModel
import java.util.Collections


class LayersAdapter(
    private val layersList: List<LayersModel>,
    private val listener: ItemTouchHelperAdapter?,

//    private val updateTouchListener: DraggableImageView,
) :
    RecyclerView.Adapter<LayersAdapter.LayerViewHolder>(), ItemTouchHelperAdapter {

    private var isDragEnabled = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayersItemBinding.inflate(inflater, parent, false)
        return LayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LayerViewHolder, position: Int) {
        val layersModel = layersList[position]
        holder.bind(layersModel)
    }

    override fun getItemCount(): Int {
        return layersList.size
    }

    inner class LayerViewHolder(private val binding: LayersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(layersModel: LayersModel) {

            binding.itemEye.setOnClickListener {

                binding.itemEye.isSelected = !binding.itemEye.isSelected

                if (binding.itemEye.isSelected) {
                    binding.itemEye.setImageResource(R.drawable.hide_eye_icon)

                } else {
                    binding.itemEye.setImageResource(R.drawable.show_eye_icon)
                }

                if (layersModel.view.visibility == View.VISIBLE) {
                    layersModel.view.visibility = View.GONE

                } else {
                    layersModel.view.visibility = View.VISIBLE
                }
            }

            binding.itemLock.setOnClickListener {

                binding.itemLock.isSelected = !binding.itemLock.isSelected

                if (binding.itemLock.isSelected) {
                    binding.itemLock.setImageResource(R.drawable.ic_lock_icon)
                } else {
                    binding.itemLock.setImageResource(R.drawable.ic_unlock_icon)
                }

                isDragEnabled = !binding.itemLock.isSelected

                /*    if (isDragEnabled) {
                        layersModel.view.setOnTouchListener(updateTouchListener)
                    } else {
                        layersModel.view.setOnTouchListener(null)
                    }*/
            }

            binding.txtV.setTextColor(Color.BLACK)
            binding.txtV.text = layersModel.viewData
            binding.executePendingBindings()

            binding.root.setOnLongClickListener {
                listener?.onStartDrag(this)
                true
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        // Swap the items in the list when moved
        Collections.swap(layersList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {

    }

    override fun updateZOrder() {

        layersList.forEachIndexed { index, layersModel ->
            layersModel.priority = index
            layersModel.view.z = index.toFloat()
        }

    }

}