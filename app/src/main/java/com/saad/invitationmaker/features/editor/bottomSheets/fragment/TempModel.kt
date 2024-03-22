package com.saad.invitationmaker.features.editor.bottomSheets.fragment

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.saad.invitationmaker.core.local.converters.BitmapConverter

@Entity(tableName = "tempSticker")
data class TempModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @TypeConverters(BitmapConverter::class)
    val stickerUrl: Bitmap,
)
