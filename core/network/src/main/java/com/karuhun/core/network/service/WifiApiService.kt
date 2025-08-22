package com.karuhun.core.network.service

import com.karuhun.core.model.Wifi
import retrofit2.http.GET

// Backend returns { code: Int, message: String, data: [Wifi] }
interface WifiApiService {
    @GET("wifi")
    suspend fun getWifi(): WifiResponse
}

// simple response wrapper
data class WifiResponse(
    val code: Int,
    val message: String,
    val data: List<Wifi>
)
