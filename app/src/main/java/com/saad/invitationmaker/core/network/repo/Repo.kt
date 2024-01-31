package com.saad.invitationmaker.core.network.repo

import androidx.lifecycle.LiveData
import com.saad.invitationmaker.core.network.models.Hit

interface Repo {

    val designsLiveData: LiveData<List<Hit>>
    val designs: LiveData<List<Hit>>
        get() = designsLiveData

    suspend fun networkCheck(category: String)
    suspend fun doNetworkCall(category: String)
}
