package com.saad.invitationmaker.features.editor.bottomSheets

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.models.TabData
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.databinding.FragmentStickersBottomSheetBinding
import com.saad.invitationmaker.features.editor.adapters.ViewPagerAdapter
import com.saad.invitationmaker.features.editor.models.stickers.SingleStickerUrlList
import com.saad.invitationmaker.features.home.adapters.HorizontalCategoryAdapter
import com.saad.invitationmaker.features.home.callbacks.HorizontalCategoryItemClick
import com.saad.invitationmaker.features.home.models.GradientColor

class StickerBottomSheet(private val callBack: (url: String) -> Unit) :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentStickersBottomSheetBinding
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var horizontalCategoryAdapter: HorizontalCategoryAdapter

    private var listOfUrls: SingleStickerUrlList? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStickersBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        horizontalRecyclerView = binding.recyclerViewHorizontal
        viewPager = binding.viewPager2
        val horizontalCategoryList = listOf(
            TabData(
                position = 0,
                "Premium",
                R.drawable.wedding_couple,
                drawableRes = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.tab_item_background_light_blue
                    )
                },
                GradientColor(
                    startColor = Color.parseColor("#F76093"),
                    endColor = Color.parseColor("#F8B3CB")
                )
            ),
            TabData(
                position = 1,
                "Freemium", R.drawable.birthday_cake,
                drawableRes = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.tab_item_background_light_blue
                    )
                },
                GradientColor(
                    startColor = Color.parseColor("#5CC5E4"),
                    endColor = Color.parseColor("#C3D9F4")
                )
            ),


            TabData(
                position = 2,
                "Ramadan", R.drawable.birthday_cake,
                drawableRes = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.tab_item_background_pink
                    )
                }, GradientColor(
                    startColor = Color.parseColor("#F76093"),
                    endColor = Color.parseColor("#F8B3CB")
                )
            ),
            TabData(
                position = 3,
                "Birthday", R.drawable.birthday_cake,
                drawableRes = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.tab_item_background_light_blue
                    )
                }, GradientColor(
                    startColor = Color.parseColor("#5CC5E4"),
                    endColor = Color.parseColor("#C3D9F4")
                )
            ),
            TabData(
                position = 4,
                "Movie Night", R.drawable.birthday_cake,
                drawableRes = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.tab_item_background_pink
                    )
                }, GradientColor(
                    startColor = Color.parseColor("#F76093"),
                    endColor = Color.parseColor("#F8B3CB")
                )
            ),
            TabData(
                position = 5,
                "Baby Shower",
                R.drawable.birthday_cake,
                drawableRes = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.tab_item_background_light_blue
                    )
                },
                GradientColor(
                    startColor = Color.parseColor("#5CC5E4"),
                    endColor = Color.parseColor("#C3D9F4")
                )
            ),
        )

        initHorizontalRecyclerView(horizontalCategoryList)

        initViewPager()

    }

    private fun initViewPager() {
        viewPagerAdapter = activity?.let { activity ->
            listOfUrls?.let { urls ->
                Utils.log("Inside Bottom Sheet adapter: $urls")
                ViewPagerAdapter(activity, urls) { sticker ->
                    callBack(sticker)
                    this.dismiss()
                }
            }
        }!!
        viewPager.adapter = viewPagerAdapter
    }

    private fun initHorizontalRecyclerView(horizontalCategoryList: List<TabData>) {
        horizontalRecyclerView.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL, false
        )

        horizontalCategoryAdapter = HorizontalCategoryAdapter(
            horizontalCategoryList,
            object : HorizontalCategoryItemClick {
                override fun itemClick(text: TabData) {
                    viewPager.currentItem = text.position
                    Utils.log("RecylerviewPositionClick: ${text.position}")

                }
            }) { position ->

            Utils.log("RecylerviewPosition: $position")
        }

        horizontalRecyclerView.adapter = horizontalCategoryAdapter
    }

    fun updateData(data: SingleStickerUrlList?) {
        if (data != null) {
            Utils.log("Inside UpdateData(): $data")
            listOfUrls = data
        } else {
            Utils.log("updateData() received null data")
        }
    }
}