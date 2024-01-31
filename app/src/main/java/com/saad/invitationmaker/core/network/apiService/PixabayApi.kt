package com.saad.invitationmaker.core.network.apiService

import com.saad.invitationmaker.BuildConfig
import com.saad.invitationmaker.core.network.models.ImageList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("?key=${BuildConfig.API_KEY}")
    suspend fun getDesignsBackground(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("q") q: String,
        @Query("orientation") orientation: String,
    ): Response<ImageList>
}