package com.saad.invitationmaker.core.di

import com.saad.invitationmaker.core.network.apiService.PixabayApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://pixabay.com/api/"

    @Singleton
    @Provides
    fun provideRetroFit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun ProvidePixaBayApi(retrofit: Retrofit): PixabayApi {
        return retrofit.create(PixabayApi::class.java)
    }
}