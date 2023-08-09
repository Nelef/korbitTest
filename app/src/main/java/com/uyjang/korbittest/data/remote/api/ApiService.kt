package com.uyjang.korbittest.data.remote.api

import com.uyjang.korbittest.data.remote.model.TickerDetail
import retrofit2.http.GET

interface ApiService {
    @GET("v1/ticker/detailed/all")
    suspend fun getTickerDetails(): Map<String, TickerDetail>
}