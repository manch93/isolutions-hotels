package com.karuhun.launcher.weather

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    // Open-Meteo API endpoint - free, no API key required
    @GET("forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,weather_code",
        @Query("temperature_unit") temperatureUnit: String = "celsius",
        @Query("timezone") timezone: String = "auto"
    ): Response<OpenMeteoResponse>
}