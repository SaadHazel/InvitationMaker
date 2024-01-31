package com.saad.invitationmaker.core.network.repo

import android.content.Context
import com.saad.invitationmaker.app.utils.Utils
import com.saad.invitationmaker.core.network.apiService.PixabayApi
import com.saad.invitationmaker.core.network.models.Hit
import com.sofit.app.utils.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoImpl @Inject constructor(
    private val context: Context,
    private val api: PixabayApi,

    ) : Repo {

    override val designsLiveData = SingleLiveEvent<List<Hit>>()
    override suspend fun networkCheck(category: String) {
        if (!Utils.isInternetAvailable(context)) {
            //Do the DB Call
        } else {
            doNetworkCall(category);
        }
    }

    override suspend fun doNetworkCall(category: String) {
        try {
            val response =
                api.getDesignsBackground(1, 20, category, "vertical")
            if (response.isSuccessful) {
                designsLiveData.postValue(response.body()?.hits)
            } else {
                //unsuccessful
            }
        } catch (e: Exception) {
            //exception
        }
    }


}
