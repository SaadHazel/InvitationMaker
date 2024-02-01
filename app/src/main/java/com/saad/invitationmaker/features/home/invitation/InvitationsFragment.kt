package com.saad.invitationmaker.features.home.invitation

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saad.invitationmaker.R
import com.saad.invitationmaker.app.models.TabData
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.databinding.FragmentInvitationsBinding
import com.saad.invitationmaker.features.home.adapters.HorizontalCategoryAdapter
import com.saad.invitationmaker.features.home.callbacks.HorizontalCategoryItemClick
import com.saad.invitationmaker.features.home.models.GradientColor

class InvitationsFragment(private val onItemClick: (String, Int) -> Unit) : Fragment() {
    private lateinit var binding: FragmentInvitationsBinding
    private lateinit var horizontalCategoryAdapter: HorizontalCategoryAdapter
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var mainRecyclerView: RecyclerView

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
        val horizontalCategoryList = listOf(
            TabData(
                position = 0,
                "wedding",
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
                "Birthday", R.drawable.birthday_cake,
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

        horizontalRecyclerView = binding.recyclerViewHorizontal
        mainRecyclerView = binding.recyclerViewVertical
        //horizontal categories setup
        horizontalRecyclerView.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL, false
        )

        horizontalCategoryAdapter = HorizontalCategoryAdapter(
            horizontalCategoryList,
            object : HorizontalCategoryItemClick {
                override fun itemClick(text: TabData) {
                    Utils.log("SelectedCategory: ${text.text}")
                }
            })

        horizontalRecyclerView.adapter = horizontalCategoryAdapter
    }
}