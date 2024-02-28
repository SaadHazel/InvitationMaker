package com.saad.invitationmaker.core.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.saad.invitationmaker.features.editor.models.CategoryModelSticker

@Dao
interface StickerDao {
    @Upsert
    fun insertData(data: List<CategoryModelSticker>)

    @Query("SELECT * FROM Sticker LIMIT 1")
    fun isStickerAvailable(): CategoryModelSticker?

    @Query("SELECT * FROM Sticker")
    fun getStickers(): List<CategoryModelSticker>
}