package com.example.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.core.domain.model.GithubUser

interface FavoriteUseCase {
    fun getAllFavUser(): LiveData<MutableList<GithubUser.Item>>
    fun insert(fav: GithubUser.Item)
    fun delete(fav: GithubUser.Item)
    suspend fun findById(id: Int): GithubUser.Item?
}