package com.example.projectone.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repo.GithubRepository
import com.example.core.domain.usecase.GithubUseCase
import com.example.core.utils.ResultViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val githubUseCase: GithubUseCase
): ViewModel() {

    val resultUser = MutableLiveData<ResultViewModel>()

    fun getUser() {
        viewModelScope.launch {
            flow {
                val response = githubUseCase.getUserFromGithub()
                emit(response)
            }.onStart {
                resultUser.value = ResultViewModel.Loading(true)
            }.onCompletion {
                resultUser.value = ResultViewModel.Loading(false)
            }.catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                resultUser.value = ResultViewModel.Error(it)
            }.collect {
                resultUser.value = ResultViewModel.Success(it)
            }
        }
    }

    fun getSearchUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = githubUseCase.searchUserFromGithub(username)
                emit(response)
            }.onStart {
                resultUser.value = ResultViewModel.Loading(true)
            }.onCompletion {
                resultUser.value = ResultViewModel.Loading(false)
            }.catch {
                Log.e("Error ", it.message.toString())
                it.printStackTrace()
                resultUser.value = ResultViewModel.Error(it)
            }.collect {
                resultUser.value = ResultViewModel.Success(it.items)
            }
        }
    }
}