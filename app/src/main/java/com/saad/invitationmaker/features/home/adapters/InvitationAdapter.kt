package com.saad.invitationmaker.features.home.adapters

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saad.invitationmaker.R
import com.saad.invitationmaker.core.extensions.setClickWithDebounce
import com.saad.invitationmaker.databinding.MainCategoryItemBinding
import com.saad.invitationmaker.features.home.models.InvitationCtModel


class InvitationAdapter(
    private val invitations: List<InvitationCtModel>,
    private val onItemClick: (String, Int) -> Unit,
) :
    RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: MainCategoryItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.main_category_item, parent, false)
        return InvitationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvitationViewHolder, position: Int) {
        holder.bind(invitations[position])
    }

    override fun getItemCount(): Int {
        Log.d("MyrecyclerView", "getItemCount: ${invitations.size}")

        return invitations.size
    }

    inner class InvitationViewHolder(private val binding: MainCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(invitation: InvitationCtModel) {
            itemView.setClickWithDebounce {
                onItemClick(binding.invitation!!.heading, binding.invitation!!.position)
            }
            binding.invitation = invitation
            Log.d("MyrecyclerView", "text: ${invitation.heading}")

            // Set gradient background
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(invitation.gradient.startColor, invitation.gradient.endColor)
            )
            gradientDrawable.cornerRadius = 10f
            binding.mainLayout.background = gradientDrawable
            // Use Glide to load the image into the ImageView
            Glide.with(binding.root)
                .load(invitation.img)
                .placeholder(R.drawable.celebrate)
                .into(binding.imgView)

            binding.executePendingBindings()
        }
    }
}
