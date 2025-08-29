# Weather Integration with Open-Meteo API

## Overview

This launcher now uses the Open-Meteo API (https://open-meteo.com/) to fetch real-time weather data based on the device's location. The Open-Meteo API is completely free and doesn't require any API key registration.

## Features

- **Real-time Weather**: Fetches current temperature and weather conditions
- **Location-based**: Uses device GPS/Network location when available
- **Fallback System**: Multiple fallback strategies for reliability
- **Caching**: Caches weather data for offline use and performance
- **No API Key Required**: Open-Meteo is completely free to use

## How it Works

### 1. Location Detection
- First attempts to get device location using Android LocationManager
- Requires `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION` permissions
- Falls back to default coordinates (New York: 40.7128, -74.0060) if location unavailable

### 2. API Integration
- Makes HTTP request to `https://api.open-meteo.com/v1/forecast`
- Requests current temperature and weather code
- Uses WMO Weather Interpretation Codes for conditions

### 3. Fallback Strategy
```
1. Try Open-Meteo API with current location
2. Try Open-Meteo API with default location  
3. Use cached weather data
4. Default to "22°C" if all else fails
```

## Usage in MainActivity

The weather integration is already set up in `MainActivity.kt`:

```kotlin
@Inject lateinit var weatherRepository: WeatherRepository

// Get current temperature
private suspend fun getTemperatureFromWeatherApi(): String {
    return try {
        val weatherData = weatherRepository.getCurrentWeather()
        weatherRepository.cacheWeatherData(weatherData)
        weatherData.temperature
    } catch (e: Exception) {
        Log.d("WeatherTemp", "Error: ${e.message}")
        "22°C" // fallback
    }
}

// Usage in Compose
LaunchedEffect(Unit) {
    currentTemperature = getTemperatureFromWeatherApi()
}
```

## API Response Format

Open-Meteo returns data in this format:

```json
{
  "current": {
    "temperature_2m": 23.5,
    "weather_code": 1
  },
  "timezone": "America/New_York"
}
```

## Weather Codes

The system interprets WMO weather codes:

- 0: Clear Sky
- 1-3: Partly Cloudy  
- 45,48: Foggy
- 51-57: Light Rain/Drizzle
- 61-67: Rain
- 71-77: Snow
- 80-86: Rain/Snow Showers
- 95-99: Thunderstorm

## Configuration

To change the default fallback location, edit the coordinates in `WeatherRepository.kt`:

```kotlin
// Default to your hotel's location
Pair(40.7128, -74.0060) // New York coordinates
```

## Permissions

The app already includes required permissions in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## Error Handling

The system gracefully handles:
- Network connectivity issues
- Location permission denials  
- API service unavailability
- Invalid API responses
- Location service failures

All errors are logged with appropriate tags for debugging.

## Testing

To test the weather integration:

1. **With Location**: Grant location permissions and test on device
2. **Without Location**: Deny permissions, should use default coordinates
3. **Offline**: Disable internet, should use cached data
4. **Cold Start**: Clear app data, should show default until API responds

## Cache System

Weather data is cached in SharedPreferences with keys:
- `last_temperature`: Most recent temperature value
- `last_description`: Weather condition description  
- `last_location`: Location name
- `last_update`: Timestamp of last successful fetch

Cache expires after reasonable time to ensure fresh data.