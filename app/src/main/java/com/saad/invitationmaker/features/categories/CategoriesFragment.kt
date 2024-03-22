package com.saad.invitationmaker.features.categories


import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
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
import com.saad.invitationmaker.app.utils.log
import com.saad.invitationmaker.core.extensions.gone
import com.saad.invitationmaker.databinding.FragmentCategoriesBinding
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel
import com.saad.invitationmaker.features.categories.adapters.CategoriesAdapter
import com.saad.invitationmaker.features.home.models.GradientColor
import com.saad.invitationmaker.features.home.models.MainCardModel


class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var viewPagerAdapter: CategoriesAdapter
    private var selectedPosition: Int = 0
    private var tabDataList: MutableList<TabData> = mutableListOf()
    private var position: Int? = null
    private var dataList: List<CategoryModel>? = null
    private var homeDataList: List<MainCardModel>? = null
    private var homeDataMutableList: MutableList<MainCardModel> = mutableListOf()
    private val listOfHit: MutableList<CategoryModel> = mutableListOf()
    private val categoryList: MutableList<String> = mutableListOf()
    private val tag = "CATEGORIESFRAGMENT"

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

        val args = arguments

        //From Home Fragment
        val homeDataArray: Array<out Parcelable>? = args?.getParcelableArray("dataList")
        homeDataList = homeDataArray?.filterIsInstance<MainCardModel>() ?: emptyList()
        log(tag, "dataList: $homeDataList")

        //From Background Fragment
        val dataArray: Array<out Parcelable>? = args?.getParcelableArray("backgroundsList")
        dataList = dataArray?.filterIsInstance<CategoryModel>() ?: emptyList()

//        val categoryList: ArrayList<CategoryModel>? =
//            arguments?.getParcelableArray("backgroundsList")
//        Log.d("CategoryTag", "$categoryList")

//        val category = getCategory!!.getString(Constants.CATEGORY)
//        position = getCategory.getInt(Constants.POSITION)
        if (dataList!!.isNotEmpty()) {
            for (cat in dataList!!) {
                listOfHit.add(cat)
            }
            position = 0
            viewPagerAdapter =
                CategoriesAdapter(
                    requireActivity(),
                    dataList = listOfHit,
                    lifecycleOwner = viewLifecycleOwner
                )


            dataList?.forEachIndexed { index, cat ->
                categoryList.add(cat.category)
                tabDataList.add(
                    TabData(
                        position = index,
                        text = cat.category,
                        drawableRes = activity?.let {
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.tab_item_background_light_blue
                            )
                        },
                        color = GradientColor(
                            startColor = Color.parseColor("#F8B3CB"),
                            endColor = Color.parseColor("#F76093")
                        )
                    ),
                )
            }
        } else if (homeDataList!!.isNotEmpty()) {
            for (cat in homeDataList!!) {
                homeDataMutableList.add(cat)
            }
            position = 0
            viewPagerAdapter =
                CategoriesAdapter(
                    requireActivity(),
                    homeDataList = homeDataMutableList,
                    lifecycleOwner = viewLifecycleOwner
                )

            homeDataList?.forEachIndexed { index, mainCardModel ->
                categoryList.add(mainCardModel.heading)
                tabDataList.add(
                    TabData(
                        position = index,
                        text = mainCardModel.heading,
                        drawableRes = activity?.let {
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.tab_item_background_light_blue
                            )
                        },
                        color = GradientColor(
                            startColor = Color.parseColor("#F8B3CB"),
                            endColor = Color.parseColor("#F76093")
                        )
                    ),
                )
            }
        }


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
            if (tabData.imageResId == 0) {
                tabImageView.gone()
            } else {
                tabData.imageResId?.let { tabImageView.setImageResource(it) }
            }

            tabLayout.addTab(tabLayout.newTab().setCustomView(customTab))
            if (categoryList.contains(tabTextView.text)) {
                customTab.isSelected = true

            }
        }

        binding.viewPager.currentItem = position ?: -1


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                /*    if (position != null) {
                        Utils.log("binding.viewPager.currentItem: ${binding.viewPager.currentItem}")
                        Utils.log("position ${tab?.position}")
                    } else {*/
                binding.viewPager.currentItem = tab?.position ?: -1

//                binding.viewPager.currentItem = position ?: -1


                updateTabAppearance(tab, true)
                selectedPosition = tab?.position ?: -1
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
}