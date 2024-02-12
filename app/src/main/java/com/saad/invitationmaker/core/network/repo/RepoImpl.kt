package com.saad.invitationmaker.core.network.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.core.di.PixabayApiService
import com.saad.invitationmaker.core.di.StickerApiService
import com.saad.invitationmaker.core.network.apiService.ApiService
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.features.editor.models.stickers.Data
import com.saad.invitationmaker.features.editor.models.stickers.Stickers
import com.sofit.app.utils.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoImpl @Inject constructor(
    private val context: Context,
    @StickerApiService private val stickerApi: ApiService,
    @PixabayApiService private val pixaBayApi: ApiService,
) : Repo {

    override val designsLiveData = SingleLiveEvent<List<Hit>>()

    override val stickerLiveData = MutableLiveData<List<Data>?>()
    override suspend fun checkNetworkAvailability(category: String) {
        if (!Utils.isInternetAvailable(context)) {
            //Do the DB Call
        } else {
            getPixaBayDesigns(category);
//            getSticker(category)
        }
    }

    override suspend fun getPixaBayDesigns(category: String) {
        try {
            val response =
                pixaBayApi.getDesignsBackground(1, 20, category, "vertical")
            if (response.isSuccessful) {
//                Utils.log("${response.body()}")
                designsLiveData.postValue(response.body()?.hits)

            } else {
                //unsuccessful
            }
        } catch (e: Exception) {
            //exception
        }
    }

    override suspend fun getSticker(term: String) {
        try {
            val response = stickerApi.getStickers(
                locale = "en-US",
                page = 1,
                limit = 20,
                order = "latest",
                term = "village",
                contentType = "vector"
            )
            if (response.isSuccessful) {
                Utils.log("${response.body()}")
                val stickerResponse: Stickers? = response.body()
                val stickerData = stickerResponse?.data
                stickerLiveData.postValue(stickerData)
            } else {
                Utils.log("Fail to get data ${response.errorBody()}")
            }
        } catch (e: Exception) {
            Utils.log("Error in catch ${e.message}")
        }
    }
}
