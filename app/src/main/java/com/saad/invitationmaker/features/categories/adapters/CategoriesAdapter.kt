package com.saad.invitationmaker.features.categories.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saad.invitationmaker.features.categories.showCategory.ShowCategoriesFragment
import com.saad.invitationmaker.features.home.greetings.GreetingsFragment

class CategoriesAdapter(
    activity: FragmentActivity,
    private val text: String,
    private val lifecycleOwner: LifecycleOwner,
//    private listOfData:List<>
) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ShowCategoriesFragment(text, lifecycleOwner)
            1 -> GreetingsFragment()
            else -> GreetingsFragment()
        }
    }
}