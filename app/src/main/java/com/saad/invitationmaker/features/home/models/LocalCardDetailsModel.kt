package com.saad.invitationmaker.features.home.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.saad.invitationmaker.features.home.converters.ViewsConverter

@Entity(tableName = "CardDetails")
data class LocalCardDetailsModel(
    @PrimaryKey
    val docId: String,
    val background: String = "",
    @TypeConverters(ViewsConverter::class)
    val views: List<AllViews>,
)
