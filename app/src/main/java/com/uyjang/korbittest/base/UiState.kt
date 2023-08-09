package com.uyjang.korbittest.base

sealed class UiState<out T> {
    object None : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T : Any>(val data: T) : UiState<T>()
    data class Error(val message: String = "", val throwable: Throwable? = null, val code: Int = 0) : UiState<Nothing>()
}