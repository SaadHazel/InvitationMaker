package com.saad.invitationmaker.core.di

import com.saad.invitationmaker.core.network.apiService.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
annotation class PIXABAY

@Qualifier
annotation class STICKERS

@Qualifier
annotation class StickerApiService

@Qualifier
annotation class PixabayApiService

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://pixabay.com/api/"
    private const val STICKER_BASE_URL = "https://api.freepik.com/"

    @PIXABAY
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @STICKERS
    @Singleton
    @Provides
    fun provideStickerRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(STICKER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @PixabayApiService
    @Singleton
    @Provides
    fun providePixabayApi(@PIXABAY retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @StickerApiService
    @Singleton
    @Provides
    fun provideStickerApi(@STICKERS stickerRetrofit: Retrofit): ApiService {
        return stickerRetrofit.create(ApiService::class.java)
    }
}
