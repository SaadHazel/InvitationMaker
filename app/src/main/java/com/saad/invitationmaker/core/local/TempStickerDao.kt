package com.saad.invitationmaker.core.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.saad.invitationmaker.features.editor.bottomSheets.fragment.TempModel

@Dao
interface TempStickerDao {
    @Upsert
    fun insertData(data: TempModel)


    @Query("SELECT * FROM tempSticker")
    fun getStickers(): TempModel
}