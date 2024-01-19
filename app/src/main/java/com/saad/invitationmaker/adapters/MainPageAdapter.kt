package com.saad.invitationmaker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saad.invitationmaker.views.fragments.GreetingsFragment
import com.saad.invitationmaker.views.fragments.InvitationsFragment

class MainPageAdapter(activity: FragmentActivity):FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
      return if(position == 0 )
            InvitationsFragment()
        else
            GreetingsFragment()
    }
}