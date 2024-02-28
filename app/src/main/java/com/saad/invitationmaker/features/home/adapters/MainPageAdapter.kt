package com.saad.invitationmaker.features.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.saad.invitationmaker.features.home.greetings.GreetingsFragment
import com.saad.invitationmaker.features.home.invitation.InvitationsFragment
import com.saad.invitationmaker.features.home.models.AllCardsDesigns
import com.saad.invitationmaker.features.home.models.MainCardModel

class MainPageAdapter(
    activity: FragmentActivity,
    private val onItemClick: (String, Int, dataList: List<MainCardModel>) -> Unit,
) :
    FragmentStateAdapter(activity) {
    private var dataList = mutableListOf<AllCardsDesigns>()
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return if (position == 0)
            InvitationsFragment(dataList, onItemClick)
        else
            GreetingsFragment()
    }

    fun inflateData(list: List<AllCardsDesigns> = listOf()) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
//        dataList = list.toMutableList()

    }
}