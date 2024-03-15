package com.example.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.core.domain.model.GithubUser

interface IFavoriteRepository {

    fun getAllFavUser(): LiveData<MutableList<GithubUser.Item>>
    fun insert(fav: GithubUser.Item)
    fun delete(fav: GithubUser.Item)
    suspend fun findById(id: Int): GithubUser.Item?
}