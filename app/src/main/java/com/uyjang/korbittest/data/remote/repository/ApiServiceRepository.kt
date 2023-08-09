package com.uyjang.korbittest.data.remote.repository

import android.util.Log
import com.uyjang.korbittest.data.remote.RetrofitClient
import com.uyjang.korbittest.data.remote.model.TickerDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiServiceRepository {
    suspend fun fetchTickerDetails(): Map<String, TickerDetail> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.getTickerDetails()
                response
            } catch (e: Exception) {
                Log.e("fetchTickerDetails", e.toString())
                emptyMap()
            }
        }
    }

}