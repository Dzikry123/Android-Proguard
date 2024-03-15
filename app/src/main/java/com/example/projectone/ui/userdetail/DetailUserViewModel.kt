package com.example.projectone.ui.userdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repo.FavoriteRepository
import com.example.core.data.repo.GithubRepository
import com.example.core.domain.model.GithubDetailUser
import com.example.core.domain.model.GithubUser
import com.example.core.domain.usecase.FavoriteUseCase
import com.example.core.domain.usecase.GithubUseCase
import com.example.core.utils.ResultViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class DetailUserViewModel @Inject constructor(
    private val githubUseCase: GithubUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    val resultDetailUser = MutableLiveData<ResultViewModel>()
    val resultFavoriteAdd = MutableLiveData<ResultViewModel>()
    val resultFavoriteDelete = MutableLiveData<ResultViewModel>()

    val resultFollowersUser = MutableLiveData<ResultViewModel>()
    val resultFollowingUser = MutableLiveData<ResultViewModel>()

    private var cachedUserData: Triple<GithubDetailUser, MutableList<GithubUser.Item>, MutableList<GithubUser.Item>>? =
        null


    // LiveData untuk status favorit
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun insert(fav: GithubUser.Item) {
        favoriteUseCase.insert(fav)
        _isFavorite.value = true
    }

    fun delete(fav: GithubUser.Item) {
        favoriteUseCase.delete(fav)
        _isFavorite.value = false
    }

    fun findById(id: Int, function: () -> Unit) {
        viewModelScope.launch {
            val user = favoriteUseCase.findById(id)
            if (user != null) {
                function()
                _isFavorite.value = true
            }
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = githubUseCase.getDetailUserFromGithub(username)
                emit(response)
            }.onStart {
                resultDetailUser.value = ResultViewModel.Loading(true)
                resultDetailUser.value = ResultViewModel.Loading(false)


            }.onCompletion {
            }.catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                resultDetailUser.value = ResultViewModel.Error(it)
            }.collect {
                // Ubah tipe data yang dikirimkan ke resultSuccess.value
                resultDetailUser.value = ResultViewModel.Success(it)

            }
        }
    }

    fun getFollowerUser(username: String) {
        var isLoading = false
        viewModelScope.launch(Dispatchers.IO) {
            var delayed = delay(2000)
            flow {
                // Emit loading state
                resultFollowersUser.postValue(ResultViewModel.Loading(true))
                val response = githubUseCase.getFollowerUserFromGithub(username)
                emit(response)
            }.onStart {
                isLoading = true
                // Start loading state
                resultFollowersUser.postValue(ResultViewModel.Loading(isLoading))
                delayed
            }.onCompletion {
                withTimeout(1000) {
                    resultFollowersUser.postValue(ResultViewModel.Loading(false))
                }
                // Complete loading state
            }.catch { throwable ->
                // Handle error
                Log.e("Error", throwable.message.toString())
                throwable.printStackTrace()
                resultFollowersUser.postValue(ResultViewModel.Error(throwable))
            }.collect { data ->
                // Emit success state
                resultFollowersUser.postValue(ResultViewModel.Success(data))
                Log.d("PENGIKUT", "${resultFollowersUser.value}")

            }
        }
    }


    fun getFollowingUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var delayed = delay(2000)
            flow {
                // Emit loading state
                resultFollowingUser.postValue(ResultViewModel.Loading(true))
                val response = githubUseCase.getFollowingUserFromGithub(username)
                emit(response)
            }.onStart {
                var isLoading = true
                // Start loading state
                resultFollowingUser.postValue(ResultViewModel.Loading(isLoading))
                delayed

            }.onCompletion {
                // Complete loading state
                withTimeoutOrNull(2000) {
                    resultFollowingUser.postValue(ResultViewModel.Loading(false))
                    Log.d("Hasil MENGIKUTI", "${ResultViewModel.Loading(false)}")

                }

            }.catch { throwable ->
                // Handle error
                Log.e("Error", throwable.message.toString())
                throwable.printStackTrace()
                resultFollowingUser.postValue(ResultViewModel.Error(throwable))
            }.collect { data ->
                // Emit success state
                resultFollowingUser.postValue(ResultViewModel.Success(data))
                Log.d("MENGIKUTI", "${resultFollowingUser.value}")
            }
        }
    }


}

