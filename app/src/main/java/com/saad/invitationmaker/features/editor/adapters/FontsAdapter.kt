package com.saad.invitationmaker.features.editor.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.databinding.SingleFontViewBinding
import com.saad.invitationmaker.features.editor.callbacks.ItemFontClickCallback
import com.saad.invitationmaker.features.editor.models.Fonts


class FontsAdapter(
    private val context: Context, private val fontsList: List<Fonts>,
    private val itemClickListener: ItemFontClickCallback,
) :
    RecyclerView.Adapter<FontsAdapter.FontsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontsViewHolder {
        val binding =
            SingleFontViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FontsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FontsViewHolder, position: Int) {
        holder.bind(fontsList[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return fontsList.size
    }

    inner class FontsViewHolder(private val binding: SingleFontViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fonts: Fonts, itemClickListener: ItemFontClickCallback) {
            val typeface = ResourcesCompat.getFont(context, fonts.fonts)
            binding.textWithFont.typeface = typeface
            binding.textWithFont.text = fonts.text
            itemView.setOnClickListener {
                itemClickListener.itemClick(fonts.fonts, fonts.text)
            }

        }
    }
}