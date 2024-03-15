package com.example.projectone.di.service

import com.example.core.domain.usecase.FavoriteInteractor
import com.example.core.domain.usecase.FavoriteUseCase
import com.example.core.domain.usecase.GithubInteractor
import com.example.core.domain.usecase.GithubUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class InteractorModlue {

    @Binds
    @Singleton
    abstract fun provideGithubUseCase(githubInteractor: GithubInteractor): GithubUseCase

    @Binds
    @Singleton
    abstract fun provideFavoriteUseCase(favoriteInteractor: FavoriteInteractor): FavoriteUseCase

}
