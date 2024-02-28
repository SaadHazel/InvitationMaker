package com.saad.invitationmaker.features.home.invitation

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.models.TabData
import com.saad.invitationmaker.app.utils.log

import com.saad.invitationmaker.databinding.FragmentInvitationsBinding
import com.saad.invitationmaker.features.editor.EditorActivity
import com.saad.invitationmaker.features.home.HomeViewModel
import com.saad.invitationmaker.features.home.adapters.HorizontalCategoryAdapter
import com.saad.invitationmaker.features.home.adapters.ParentRecyclerAdapter
import com.saad.invitationmaker.features.home.callbacks.HorizontalCategoryItemClick
import com.saad.invitationmaker.features.home.callbacks.SingleDesignCallBack
import com.saad.invitationmaker.features.home.models.AllCardsDesigns
import com.saad.invitationmaker.features.home.models.GradientColor
import com.saad.invitationmaker.features.home.models.MainCardModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class InvitationsFragment(
    private val dataList: List<AllCardsDesigns>,
    private val onItemClick: (String, Int, List<MainCardModel>) -> Unit,
) :
    Fragment() {
    private lateinit var binding: FragmentInvitationsBinding
    private lateinit var horizontalCategoryAdapter: HorizontalCategoryAdapter
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var mainRecyclerView: RecyclerView
    private var listData: MutableList<MainCardModel> = mutableListOf()
    private var horizontalCategoryList: MutableList<TabData> = mutableListOf()
    private val allCard = mutableSetOf<String>()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInvitationsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyrecyclerView", "getItemCount: Inside my fragment")
        init()

    }

    private fun init() {

        dataList.forEach { data ->
            allCard.add(data.category)
        }
        allCard.forEachIndexed { index, allCardsDesigns ->
            horizontalCategoryList.add(
                TabData(
                    position = index,
                    allCardsDesigns,
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

        horizontalRecyclerView = binding.recyclerViewHorizontal
        mainRecyclerView = binding.recyclerViewVertical
        //horizontal categories setup

        mainRecycleView(mainRecyclerView)
        horizontalTabLayoutRecyclerView(horizontalRecyclerView, horizontalCategoryList)

    }

    private fun horizontalTabLayoutRecyclerView(
        horizontalRecyclerView: RecyclerView,
        horizontalCategoryList: List<TabData>,
    ) {
        horizontalRecyclerView.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL, false
        )

        horizontalCategoryAdapter = HorizontalCategoryAdapter(
            horizontalCategoryList,
            object : HorizontalCategoryItemClick {
                override fun itemClick(text: TabData) {
                    //Callback to mainFragment
                    onItemClick(text.text, text.position, listData)
                }
            }
        ) {}

        horizontalRecyclerView.adapter = horizontalCategoryAdapter
    }

    private fun mainRecycleView(mainRecyclerView: RecyclerView) {
        allCard.forEach { category ->
            listData.add(
                MainCardModel(
                    heading = category,
                    imageIcon = R.drawable.fire_icon,
                    allDesigns = dataList.filter { it.category == category }
                ),
            )
        }

        mainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mainRecyclerView.adapter = ParentRecyclerAdapter(listData, object : SingleDesignCallBack {
            override fun onDesignClick(category: String, docId: String) {
                log("InvitaionFragmentCategory", "Category: $category")
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getSingleCardDesign(category, docId)
                    withContext(Dispatchers.Main) {
                        val i = Intent(activity, EditorActivity::class.java)
                        i.putExtra("docId", docId)
                        i.putExtra("category", category)
                        startActivity(i)
                        (activity as Activity?)!!.overridePendingTransition(0, 0)
                    }
                }

            }
        })
    }
}