package com.saad.invitationmaker.features.backgrounds.callbacks

import com.saad.invitationmaker.features.backgrounds.models.CategoryModel

interface BackgroundCallBack {
    fun onBackgroundClick(url: String)

    fun onCategoryClick(data: List<CategoryModel>)
}