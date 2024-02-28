package com.saad.invitationmaker.core.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel


@Dao
interface BackgroundDao {
    @Upsert
    fun insertData(data: List<CategoryModel>)

    @Query("SELECT * FROM Backgrounds LIMIT 1")
    fun isBackgroundAvailable(): CategoryModel?

    @Query("SELECT * FROM Backgrounds")
    fun getBackgrounds(): List<CategoryModel>

}