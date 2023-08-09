package com.uyjang.korbittest.data.remote.repository

import android.util.Log
import com.uyjang.korbittest.data.remote.RetrofitClient
import com.uyjang.korbittest.data.remote.model.TickerDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class ApiResult<T> {
    class Loading<T> : ApiResult<T>()
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val exception: Throwable) : ApiResult<T>()
}

class ApiServiceRepository {
    fun fetchTickerDetails(): Flow<ApiResult<Map<String, TickerDetail>>> = flow {
        emit(ApiResult.Loading())  // 로딩 상태 전송
        try {
            val response = RetrofitClient.apiService.getTickerDetails()
            emit(ApiResult.Success(response))  // 성공 상태 및 데이터 전송
        } catch (e: Exception) {
            Log.e("fetchTickerDetails", e.toString())
            emit(ApiResult.Error(e))  // 에러 상태 전송
        }
    }
}