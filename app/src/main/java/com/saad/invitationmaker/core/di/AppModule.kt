package com.saad.invitationmaker.core.di

import android.content.Context
import com.saad.invitationmaker.core.network.repo.Repo
import com.saad.invitationmaker.core.network.repo.RepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideRepo(impl: RepoImpl): Repo {
        return impl
    }

}