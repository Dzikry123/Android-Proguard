package com.example.core.utils

sealed class ResultViewModel {
    data class Success<out T>(val data: T) : ResultViewModel()
    data class Error(val exception: Throwable) : ResultViewModel()
    data class Loading(val isLoading: Boolean) : ResultViewModel()
}
