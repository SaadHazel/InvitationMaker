package com.saad.invitationmaker.core.network.apiService

import com.saad.invitationmaker.BuildConfig
import com.saad.invitationmaker.core.network.models.ImageList
import com.saad.invitationmaker.features.editor.models.stickers.Stickers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @GET("?key=${BuildConfig.API_KEY}")
    suspend fun getDesignsBackground(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("q") q: String,
        @Query("orientation") orientation: String,
    ): Response<ImageList>

    @Headers("X-Freepik-API-Key: ${BuildConfig.STICKERS_API_KEY}")
    @GET("v1/resources")
    suspend fun getStickers(
        @Query("locale") locale: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("order") order: String,
        @Query("term") term: String,
        @Query("content_type") contentType: String,
    ): Response<Stickers>
}