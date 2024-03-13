package com.example.favorite.ui.fav

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.data.repo.FavoriteRepository


class FavoriteViewModel (application: Application) : ViewModel() {
    private val mFavRepo: FavoriteRepository =
        FavoriteRepository(application)
    fun getAllFavUser() = mFavRepo.getAllFavUser()
}

class FavoriteViewModelFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(application) as T
        }
        throw IllegalAccessException("Unkwon ViewModel :" + modelClass.name)
    }
}