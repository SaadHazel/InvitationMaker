package com.saad.invitationmaker.features.categories.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel
import com.saad.invitationmaker.features.categories.showCategory.ShowCategoriesFragment
import com.saad.invitationmaker.features.home.models.MainCardModel

class CategoriesAdapter(
    activity: FragmentActivity,
    private val dataList: List<CategoryModel>? = null,
    private val homeDataList: List<MainCardModel>? = null,
    private val lifecycleOwner: LifecycleOwner,
) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return dataList?.size ?: homeDataList!!.size

    }

    /*   override fun createFragment(position: Int): Fragment {

           return when (position) {
               0 -> {
                   if (dataList == null) {
                       Log.d("ShowCategoryList", "list: ${homeDataList?.get(0)?.allDesigns}")

                       ShowCategoriesFragment(
                           homeDataList = homeDataList?.get(0)?.allDesigns,
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   } else {
                       Log.d("ShowCategoryList", "dataList[0]: ${dataList[0]}")

                       ShowCategoriesFragment(
                           category = dataList[0],
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   }
               }

               1 -> {
                   if (dataList == null) {
                       ShowCategoriesFragment(
                           homeDataList = homeDataList?.get(1)?.allDesigns,
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   } else {
                       ShowCategoriesFragment(
                           category = dataList[1],
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   }
               }

               3 -> {
                   if (dataList == null) {
                       ShowCategoriesFragment(
                           homeDataList = homeDataList?.get(2)?.allDesigns,
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   } else {
                       ShowCategoriesFragment(
                           category = dataList[2],
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   }
               }

               4 -> {
                   if (dataList == null) {
                       ShowCategoriesFragment(
                           homeDataList = homeDataList?.get(0)?.allDesigns,
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   } else {
                       ShowCategoriesFragment(
                           category = dataList[3],
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   }
               }

               else -> {
                   if (dataList == null) {
                       ShowCategoriesFragment(
                           homeDataList = homeDataList?.get(1)?.allDesigns,
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   } else {
                       ShowCategoriesFragment(
                           category = dataList[3],
                           parentLifeCycleOwner = lifecycleOwner
                       )
                   }
               }
           }
       }*/
    override fun createFragment(position: Int): Fragment {
        val categoryIndex = position % maxOf(dataList?.size ?: 0, homeDataList?.size ?: 0)

        val category = dataList?.getOrNull(categoryIndex)
        val homeData = homeDataList?.getOrNull(categoryIndex)?.allDesigns

        return when {
            dataList != null -> {
                // Scenario one: dataList is provided
                ShowCategoriesFragment(
                    category = category,
                    parentLifeCycleOwner = lifecycleOwner
                )
            }

            homeDataList != null -> {
                // Scenario two: homeDataList is provided
                ShowCategoriesFragment(
                    homeDataList = homeData,
                    parentLifeCycleOwner = lifecycleOwner
                )
            }

            else -> {
                Fragment()
            }
        }
    }

}