package com.karuhun.launcher.weather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val weatherApi: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    // Open-Meteo API is free and doesn't require API key

    suspend fun getCurrentWeather(): WeatherData {
        return withContext(Dispatchers.IO) {
            try {
                // Try to get location-based weather
                val location = getCurrentLocation()
                val (lat, lon, locationSource) = if (location != null) {
                    Log.d("WeatherRepository", "Using device location: ${location.first}, ${location.second}")
                    Triple(location.first, location.second, "Device Location")
                } else {
                    Log.d("WeatherRepository", "Using hotel default location")
                    // Your hotel's coordinates
                    Triple(40.1853, 44.5121, "Hotel Default Location")
                }
                
                Log.d("WeatherRepository", "Fetching weather for coordinates: $lat, $lon ($locationSource)")
                
                val response = weatherApi.getCurrentWeather(
                    latitude = lat,
                    longitude = lon
                )

                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        val temp = weatherResponse.current.temperature.toInt()
                        val description = getWeatherDescription(weatherResponse.current.weatherCode)
                        
                        val weatherData = WeatherData(
                            temperature = "${temp}°C",
                            description = description,
                            location = locationSource,
                            weatherCode = weatherResponse.current.weatherCode
                        )
                        
                        // Cache the successful result and location
                        cacheWeatherData(weatherData, lat, lon)
                        
                        Log.d("WeatherRepository", "Weather fetched successfully: $temp°C, $description")
                        return@withContext weatherData
                    }
                }

                Log.w("WeatherRepository", "API response was not successful, using cached weather")
                // If API fails, try to get cached weather
                getCachedWeather()
            } catch (e: Exception) {
                Log.e("WeatherRepository", "Error fetching weather: ${e.message}")
                getCachedWeather()
            }
        }
    }

    /**
     * Convert Open-Meteo weather codes to descriptive text
     * Based on WMO Weather interpretation codes
     */
    private fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Clear Sky"
            1, 2, 3 -> "Partly Cloudy"
            45, 48 -> "Foggy"
            51, 53, 55 -> "Light Rain"
            56, 57 -> "Freezing Rain"
            61, 63, 65 -> "Rain"
            66, 67 -> "Freezing Rain"
            71, 73, 75 -> "Snow"
            77 -> "Snow Grains"
            80, 81, 82 -> "Rain Showers"
            85, 86 -> "Snow Showers"
            95 -> "Thunderstorm"
            96, 99 -> "Thunderstorm with Hail"
            else -> "Clear"
        }
    }
    
    private fun getCurrentLocation(): Pair<Double, Double>? {
        return try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("WeatherRepository", "Location permissions not granted")
                return null
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            
            // Check if location services are enabled
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            val isPassiveEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
            Log.d("WeatherRepository", "GPS enabled: $isGpsEnabled, Network enabled: $isNetworkEnabled, Passive enabled: $isPassiveEnabled")
            
            // For Android TV, prioritize Network Location over GPS
            if (!isNetworkEnabled && !isGpsEnabled && !isPassiveEnabled) {
                Log.d("WeatherRepository", "All location services are disabled")
                return getLocationFromSystemSources()
            }
            
            // Try to get the most recent location from available providers
            // For Android TV, prioritize Network Location (uses WiFi positioning)
            var bestLocation: android.location.Location? = null
            
            // First try Network Location (most suitable for Android TV)
            if (isNetworkEnabled) {
                val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                Log.d("WeatherRepository", "Network location: $networkLocation")
                if (networkLocation != null) {
                    bestLocation = networkLocation
                    Log.d("WeatherRepository", "Using Network location: ${networkLocation.latitude}, ${networkLocation.longitude} (age: ${(System.currentTimeMillis() - networkLocation.time) / 1000}s)")
                }
            }
            
            // Fallback to GPS if Network location is not available
            if (bestLocation == null && isGpsEnabled) {
                val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                Log.d("WeatherRepository", "GPS location: $gpsLocation")
                if (gpsLocation != null) {
                    bestLocation = gpsLocation
                    Log.d("WeatherRepository", "Using GPS location: ${gpsLocation.latitude}, ${gpsLocation.longitude} (age: ${(System.currentTimeMillis() - gpsLocation.time) / 1000}s)")
                }
            }
            
            // Fallback to Passive location
            if (bestLocation == null && isPassiveEnabled) {
                val passiveLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                Log.d("WeatherRepository", "Passive location: $passiveLocation")
                if (passiveLocation != null) {
                    bestLocation = passiveLocation
                    Log.d("WeatherRepository", "Using Passive location: ${passiveLocation.latitude}, ${passiveLocation.longitude} (age: ${(System.currentTimeMillis() - passiveLocation.time) / 1000}s)")
                }
            }
            
            bestLocation?.let { 
                Log.d("WeatherRepository", "Using location: ${it.latitude}, ${it.longitude} from ${it.provider} (age: ${(System.currentTimeMillis() - it.time) / 1000}s)")
                Pair(it.latitude, it.longitude) 
            } ?: run {
                Log.d("WeatherRepository", "No location from LocationManager, trying system sources")
                getLocationFromSystemSources()
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error getting location: ${e.message}")
            getLocationFromSystemSources()
        }
    }
    
    private fun getLocationFromSystemSources(): Pair<Double, Double>? {
        return try {
            // Try to read cached location from our own shared preferences
            val prefs = context.getSharedPreferences("weather_cache", Context.MODE_PRIVATE)
            val cachedLat = prefs.getString("cached_latitude", null)
            val cachedLon = prefs.getString("cached_longitude", null)
            
            if (cachedLat != null && cachedLon != null) {
                try {
                    val lat = cachedLat.toDouble()
                    val lon = cachedLon.toDouble()
                    Log.d("WeatherRepository", "Using previously cached location: $lat, $lon")
                    return Pair(lat, lon)
                } catch (e: Exception) {
                    Log.d("WeatherRepository", "Invalid cached location data")
                }
            }
            
            Log.d("WeatherRepository", "No location available from any source")
            null
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error getting location from system sources: ${e.message}")
            null
        }
    }

    private fun getCachedWeather(): WeatherData {
        val sharedPrefs = context.getSharedPreferences("weather_cache", Context.MODE_PRIVATE)
        val cachedTemp = sharedPrefs.getString("last_temperature", "22°C")
        val cachedDescription = sharedPrefs.getString("last_description", "Clear")
        val cachedLocation = sharedPrefs.getString("last_location", "Unknown")
        val cachedWeatherCode = sharedPrefs.getInt("last_weather_code", 0) // Default to clear sky
        
        return WeatherData(
            temperature = cachedTemp ?: "22°C",
            description = cachedDescription ?: "Clear",
            location = cachedLocation ?: "Unknown",
            weatherCode = cachedWeatherCode
        )
    }

    fun cacheWeatherData(weatherData: WeatherData, latitude: Double? = null, longitude: Double? = null) {
        val sharedPrefs = context.getSharedPreferences("weather_cache", Context.MODE_PRIVATE)
        sharedPrefs.edit().apply {
            putString("last_temperature", weatherData.temperature)
            putString("last_description", weatherData.description)
            putString("last_location", weatherData.location)
            putInt("last_weather_code", weatherData.weatherCode)
            putLong("last_update", System.currentTimeMillis())
            
            // Cache location coordinates if available
            if (latitude != null && longitude != null) {
                putString("cached_latitude", latitude.toString())
                putString("cached_longitude", longitude.toString())
            }
            
            apply()
        }
    }
}