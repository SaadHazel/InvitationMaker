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
import com.saad.invitationmaker.app.utils.log
import com.saad.invitationmaker.databinding.FragmentStickersBottomSheetBinding
import com.saad.invitationmaker.features.editor.adapters.ViewPagerAdapter
import com.saad.invitationmaker.features.editor.models.CategoryModelSticker
import com.saad.invitationmaker.features.home.adapters.HorizontalCategoryAdapter
import com.saad.invitationmaker.features.home.callbacks.HorizontalCategoryItemClick
import com.saad.invitationmaker.features.home.models.GradientColor

class StickerBottomSheet(private val callBack: (url: String) -> Unit) :
    BottomSheetDialogFragment() {
    private val tag = "StickerBottomSheet"
    private lateinit var binding: FragmentStickersBottomSheetBinding
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var horizontalCategoryAdapter: HorizontalCategoryAdapter
    private val horizontalCategoryList: MutableList<TabData> = mutableListOf()
    private val allStickerOptions = mutableSetOf<String>()

    private var listOfUrls: List<CategoryModelSticker>? = null
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
        horizontalCategoryList.clear()
        listOfUrls?.forEach { data ->
            allStickerOptions.add(data.category)
        }
        allStickerOptions.forEachIndexed { index, category ->
            horizontalCategoryList.add(
                TabData(
                    position = index,
                    category,
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
                )
            )
        }
        log(tag, " setSize: ${allStickerOptions.size}")


        initHorizontalRecyclerView(horizontalCategoryList)

        initViewPager()

    }

    private fun initViewPager() {
        viewPagerAdapter = activity?.let { activity ->
            listOfUrls?.let { urls ->
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


                }
            }) { position ->
        }

        horizontalRecyclerView.adapter = horizontalCategoryAdapter
    }

    fun updateData(data: List<CategoryModelSticker>?) {
        if (data != null) {
            listOfUrls = data
        }
    }

    override fun onPause() {
        super.onPause()
        log(tag, "onPause - setSize: ${allStickerOptions.size}")
//        allStickerOptions.clear()
    }

    override fun dismiss() {
        super.dismiss()
        log(tag, "onDismiss")
        allStickerOptions.clear()
    }
}