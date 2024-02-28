package com.saad.invitationmaker.core.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.saad.invitationmaker.features.home.models.AllCardsDesigns
import com.saad.invitationmaker.features.home.models.LocalCardDetailsModel

@Dao
interface CardsDao {

    @Upsert
    suspend fun insertData(data: List<AllCardsDesigns>)


    @Query("SELECT * FROM AllCards")
    suspend fun getData(): List<AllCardsDesigns>

    @Query("SELECT * FROM AllCards LIMIT 1")
    fun getAnyCardDetail(): AllCardsDesigns?

    @Query("SELECT * FROM CardDetails WHERE docId == :docId LIMIT 1")
    suspend fun isCardDetailAvailable(docId: String): LocalCardDetailsModel?

    @Upsert
    suspend fun insertCardsDetail(data: LocalCardDetailsModel)

    @Query("SELECT * FROM CardDetails WHERE docId == :docId")
    suspend fun getSingleCardDetails(docId: String): LocalCardDetailsModel

}