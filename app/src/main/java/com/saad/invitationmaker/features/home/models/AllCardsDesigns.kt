package com.saad.invitationmaker.features.home.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "AllCards")
data class AllCardsDesigns(
    @PrimaryKey
    val docId: String,
    val thumbnail: String,
    val category: String = "",
) : Parcelable