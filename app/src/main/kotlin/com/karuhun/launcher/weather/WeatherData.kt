package com.karuhun.launcher.weather

import com.google.gson.annotations.SerializedName

// Open-Meteo API response structure
data class OpenMeteoResponse(
    @SerializedName("current")
    val current: CurrentWeather,
    @SerializedName("timezone")
    val timezone: String
)

data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("weather_code")
    val weatherCode: Int
)

// Location data for API requests
data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val city: String = "Unknown Location"
)

// Our internal weather data model
data class WeatherData(
    val temperature: String,
    val description: String,
    val location: String,
    val weatherCode: Int = 0 // Default to clear sky
)

// Legacy models for compatibility (if needed elsewhere)
data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)