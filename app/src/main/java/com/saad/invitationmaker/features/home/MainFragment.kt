package com.saad.invitationmaker.features.home

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.saad.invitationmaker.R
import com.saad.invitationmaker.core.extensions.navigate
import com.saad.invitationmaker.databinding.FragmentMainBinding
import com.saad.invitationmaker.features.home.adapters.MainPageAdapter
import com.saad.invitationmaker.features.home.models.TabDataMain


//@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewPagerAdapter: MainPageAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var tabDataList: List<TabDataMain>
//    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        tabDataList = listOf(
            TabDataMain(getString(R.string.invitation), R.drawable.tab_item_background_main),
            TabDataMain(getString(R.string.greetings), R.drawable.tab_item_background_main)
        )
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        viewPagerAdapter = MainPageAdapter(requireActivity()) { category, position ->
            navigate(
                R.id.nav_host_fragment,
                MainFragmentDirections.toCategoriesFragment(
                    category = category,
                    position = position
                )
            )

        }

        binding.viewPager.adapter = viewPagerAdapter

        for (tabData in tabDataList) {
            val customTab = LayoutInflater.from(requireContext())
                .inflate(R.layout.custom_tab_item_main, null) as ConstraintLayout
            customTab.setBackgroundResource(tabData.drawableRes)
            val tabTextView = customTab.findViewById<TextView>(R.id.text2)
            tabTextView.text = tabData.text

            tabLayout.addTab(tabLayout.newTab().setCustomView(customTab))
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager.currentItem = tab.position
                    updateTabAppearance(tab, true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    private fun updateTabAppearance(tab: TabLayout.Tab, isSelected: Boolean) {
        val customTab = tab.customView as? ConstraintLayout
        val tabTextView = customTab?.findViewById<TextView>(R.id.text2)
        if (isSelected) {
            tabTextView?.setTypeface(null, Typeface.BOLD)
        } else {
            tabTextView?.setTypeface(null, Typeface.NORMAL)

        }
    }
}