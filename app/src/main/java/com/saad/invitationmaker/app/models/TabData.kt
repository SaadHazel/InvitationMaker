package com.saad.invitationmaker.app.models

import android.graphics.drawable.Drawable
import com.saad.invitationmaker.features.home.models.GradientColor

data class TabData(
    val position: Int,
    val text: String,
    val imageResId: Int? = 0,
    val drawableRes: Drawable?,
    val color: GradientColor,
)

