package com.saad.invitationmaker.features.editor.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.features.backgrounds.converter.BackgroundConverter


@Entity(tableName = "Sticker")
data class CategoryModelSticker(
    @PrimaryKey
    val category: String,
    @TypeConverters(BackgroundConverter::class)
    val hit: List<Hit>,
)