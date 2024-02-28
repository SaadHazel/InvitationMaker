package com.saad.invitationmaker.features.backgrounds.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.features.backgrounds.converter.BackgroundConverter
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "Backgrounds")
data class CategoryModel(
    @PrimaryKey
    val category: String,
    @TypeConverters(BackgroundConverter::class)
    val hit: List<Hit>,
) : Parcelable