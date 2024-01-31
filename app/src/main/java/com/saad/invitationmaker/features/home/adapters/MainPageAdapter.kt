package com.saad.invitationmaker.features.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saad.invitationmaker.features.home.greetings.GreetingsFragment
import com.saad.invitationmaker.features.home.invitation.InvitationsFragment

class MainPageAdapter(activity: FragmentActivity, private val onItemClick: (String, Int) -> Unit) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            InvitationsFragment(onItemClick)
        else
            GreetingsFragment()
    }
}