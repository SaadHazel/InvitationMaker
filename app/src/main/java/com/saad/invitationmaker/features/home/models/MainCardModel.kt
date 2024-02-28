package com.saad.invitationmaker.features.home.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainCardModel(
    val heading: String,
    val imageIcon: Int,
    val allDesigns: List<AllCardsDesigns>,
) : Parcelable