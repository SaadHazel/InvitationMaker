package com.saad.invitationmaker.core.repo

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel
import com.saad.invitationmaker.features.editor.bottomSheets.fragment.TempModel
import com.saad.invitationmaker.features.editor.models.CategoryModelSticker
import com.saad.invitationmaker.features.home.models.AllCardsDesigns
import com.saad.invitationmaker.features.home.models.LocalCardDetailsModel
import kotlinx.coroutines.flow.Flow

interface Repo {

    val designsLiveData: LiveData<List<Hit>>
    val designs: LiveData<List<Hit>>
        get() = designsLiveData

    /*    val stickerLiveData: LiveData<List<Hit>>
        val stickers: LiveData<List<Hit>>
            get() = stickerLiveData*/

    //    val getAllDataFromFireStore: Flow<List<AllCardsDesigns>>
    suspend fun getAllDataFromFireStore()

    val getAllDataFromLocal: Flow<List<AllCardsDesigns>>


    val getAllBackgrounds: Flow<List<CategoryModel>>
    val getAllStickerss: Flow<List<CategoryModelSticker>>

    suspend fun getAllBackgroundsRemote()

    suspend fun getAllStickersRemote()

    suspend fun saveSingleCardLocally(category: String, docId: String)

    fun getSingleCardDetailsLocal(
        category: String,
        docId: String,
    ): Flow<LocalCardDetailsModel>

    /*    suspend fun saveGreetingToWeddingCollection(dataList: LocalCardDetailsModel)*/
    suspend fun isDataBaseEmpty(): Boolean

    suspend fun isBackgroundDataBaseEmpty(): Boolean
    suspend fun isStickerDataBaseEmpty(): Boolean
    suspend fun isCardDetailsAvailable(docId: String): Boolean

    suspend fun checkNetworkAvailability(category: String)
    suspend fun getPixaBayDesigns(category: String)

//    suspend fun getSticker(category: String)

    suspend fun singleDesignCall(category: String, docId: String)

    //Download Image as bitmap
    suspend fun storeImage(img: String)

    suspend fun downloadBitmap(imageUrl: String): Bitmap?

    //TempSticker
    suspend fun saveTempStickerToDB(sticker: TempModel)

    suspend fun getTempStickerFromRoom(): TempModel

}
