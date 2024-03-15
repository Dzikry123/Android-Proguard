package com.example.core.di

import android.content.Context
import com.example.core.data.service.ApiClient
import com.example.core.data.service.ServiceGithub
import com.example.core.data.repo.FavoriteRepository
import com.example.core.domain.repository.IGithubRepository
import com.example.core.data.repo.GithubRepository
import com.example.core.domain.repository.IFavoriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideGithubUserListRepository(service: ServiceGithub): IGithubRepository {
        return GithubRepository(service)
    }

    @Provides
    @Singleton
    fun provideIFavoriteRepository(@ApplicationContext context: Context): IFavoriteRepository {
        return FavoriteRepository(context)
    }

    @Provides
    @Singleton
    fun provideServiceGithub(): ServiceGithub {
        return ApiClient.githubService
    }

}