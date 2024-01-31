package com.saad.invitationmaker.features.home.models

data class InvitationCtModel(
    val position: Int,
    val heading: String,
    val img: String,
    val gradient: GradientColor,
)

data class GradientColor(
    val startColor: Int,
    val endColor: Int,
)

