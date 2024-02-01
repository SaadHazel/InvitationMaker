package com.saad.invitationmaker.features.categories


import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.models.TabData
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.app.utils.constants.Constants
import com.saad.invitationmaker.databinding.FragmentCategoriesBinding
import com.saad.invitationmaker.features.categories.adapters.CategoriesAdapter
import com.saad.invitationmaker.features.home.models.GradientColor


class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var viewPagerAdapter: CategoriesAdapter
    private var selectedPosition: Int = 0
    private lateinit var tabDataList: List<TabData>
    private var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.tabLayout
        val getCategory = arguments
        val category = getCategory!!.getString(Constants.CATEGORY)
        position = getCategory.getInt(Constants.POSITION)
        Utils.log("position from fragment: $position")
        viewPagerAdapter =
            CategoriesAdapter(requireActivity(), category!!, lifecycleOwner = viewLifecycleOwner)

        tabDataList = listOf(
            TabData(
                position = 0,
                "wedding",
                R.drawable.wedding_couple,
                drawableRes = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.tab_item_background_pink
                    )
                },
                GradientColor(
                    startColor = Color.parseColor("#F76093"),
                    endColor = Color.parseColor("#F8B3CB")
                )
            )
        )
        binding.viewPager.adapter = viewPagerAdapter

        goBack()

        for (tabData in tabDataList) {
            val customTab = LayoutInflater.from(requireContext())
                .inflate(R.layout.custom_tab_item, null) as ConstraintLayout
//            customTab.setBackgroundResource(tabData.drawableRes)
            val tabTextView = customTab.findViewById<TextView>(R.id.text1)
            tabTextView.text = tabData.text

            textGradient(tabTextView, tabData.color.startColor, tabData.color.endColor)

            val tabImageView =
                customTab.findViewById<ImageView>(R.id.img1)

            tabImageView.setImageResource(tabData.imageResId)

            tabLayout.addTab(tabLayout.newTab().setCustomView(customTab))
            if (tabTextView.text == category) {
                customTab.isSelected = true
            }
        }

        binding.viewPager.currentItem = position ?: -1
        Utils.log("binding.viewPager.currentItem: ${binding.viewPager.currentItem}")

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                /*    if (position != null) {
                        Utils.log("binding.viewPager.currentItem: ${binding.viewPager.currentItem}")
                        Utils.log("position ${tab?.position}")
                    } else {*/
                binding.viewPager.currentItem = tab?.position ?: -1

//                binding.viewPager.currentItem = position ?: -1

                Utils.log("tab_position ${tab?.position}")

                updateTabAppearance(tab, true)
                selectedPosition = tab?.position ?: -1
//                Utils.log("position $selectedPosition")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                updateTabAppearance(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        updateTabAppearance(tabLayout.getTabAt(position!!), true)
    }

    private fun textGradient(tabTextView: TextView, startColor: Int, endColor: Int) {
        val shader: Shader = LinearGradient(
            0f, 0f, 0f, tabTextView.lineHeight.toFloat(),
            startColor, endColor, Shader.TileMode.REPEAT
        )
        tabTextView.paint.setShader(shader)
    }


    private fun goBack() = binding.apply {
        backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateTabAppearance(tab: TabLayout.Tab?, isSelected: Boolean) {
        val customTab = tab?.customView as? ConstraintLayout
        val tabTextView = customTab?.findViewById<TextView>(R.id.text1)
        val customWhileBackgroundView = customTab?.findViewById<View>(R.id.white_view)
        if (isSelected) {
            if (tabTextView != null) {
                textGradient(tabTextView, Color.WHITE, Color.WHITE)
            }
            tabTextView?.setTypeface(null, Typeface.BOLD)
            customWhileBackgroundView?.visibility = View.INVISIBLE
        } else {
            tabDataList.forEach {
                if (tab?.position == it.position) {
                    if (tabTextView != null) {
                        textGradient(tabTextView, it.color.startColor, it.color.endColor)
                    }
                }
            }
            tabTextView?.setTypeface(null, Typeface.NORMAL)
            customWhileBackgroundView?.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        Utils.log("Fragment onPause Parent")
    }

    override fun onStop() {
        super.onStop()
        Utils.log("Fragment onStop Parent")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utils.log("View Destroyed Parent")
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.log("Fragment Destroyed Parent")
    }

    override fun onDetach() {
        super.onDetach()
        Utils.log("Fragment onDetach Parent")
    }

}