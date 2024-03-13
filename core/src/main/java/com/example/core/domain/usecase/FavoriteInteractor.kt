package com.example.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.core.domain.model.GithubUser
import com.example.core.domain.repository.IFavoriteRepository
import javax.inject.Inject

class FavoriteInteractor @Inject constructor(private val repository: IFavoriteRepository): FavoriteUseCase {
    override fun getAllFavUser(): LiveData<MutableList<GithubUser.Item>> = repository.getAllFavUser()

    override fun insert(fav: GithubUser.Item) = repository.insert(fav)

    override fun delete(fav: GithubUser.Item) = repository.delete(fav)

    override suspend fun findById(id: Int): GithubUser.Item? = repository.findById(id)
}