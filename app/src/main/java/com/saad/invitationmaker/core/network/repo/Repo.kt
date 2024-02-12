package com.saad.invitationmaker.core.network.repo

import androidx.lifecycle.LiveData
import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.features.editor.models.stickers.Data

interface Repo {

    val designsLiveData: LiveData<List<Hit>>
    val designs: LiveData<List<Hit>>
        get() = designsLiveData


    val stickerLiveData: LiveData<List<Data>?>
    val stickers: LiveData<List<Data>?>
        get() = stickerLiveData

    suspend fun checkNetworkAvailability(category: String)
    suspend fun getPixaBayDesigns(category: String)

    suspend fun getSticker(term: String)
}
