# Hotel Location Configuration

## Current Configuration
The weather system is currently set to use **New York City** coordinates as the default location when device location is not available.

**Current Coordinates**: 40.7128, -74.0060 (New York City)

## To Set Your Hotel's Location

1. **Find Your Coordinates**:
   - Go to [Google Maps](https://www.google.com/maps)
   - Navigate to your hotel location
   - Right-click on the exact location
   - Select "What's here?"
   - Copy the coordinates (format: latitude, longitude)

2. **Update the Code**:
   - Open: `app/src/main/kotlin/com/karuhun/launcher/weather/WeatherRepository.kt`
   - Find line ~54: `Triple(40.7128, -74.0060, "Hotel Default Location")`
   - Replace with your coordinates: `Triple(YOUR_LAT, YOUR_LONG, "Your Hotel Name")`

## Example Locations

```kotlin
// New York (current default)
Triple(40.7128, -74.0060, "Hotel Default Location")

// London
Triple(51.5074, -0.1278, "London Hotel")

// Dubai
Triple(25.2048, 55.2708, "Dubai Hotel")

// Tokyo
Triple(35.6762, 139.6503, "Tokyo Hotel")

// Sydney
Triple(-33.8688, 151.2093, "Sydney Hotel")
```

## Network Location for Android TV (Recommended)

**Network Location** is the best option for Android TV devices as it uses:
- **WiFi Network Positioning**: Uses nearby WiFi networks to determine location
- **No GPS Required**: Works without satellite reception
- **Indoor-friendly**: Perfect for hotel/indoor environments
- **More Accurate**: Often more precise than GPS indoors

### How to Enable Network Location:

1. **Android TV Settings** → **Device Preferences** → **Location** (or **Privacy** → **Location**)
2. Turn ON **Location Services**
3. **Make sure "Network Location" is enabled** ✅
4. **Optional**: Turn off GPS if not needed
5. Grant location permissions to the launcher app

### What Network Location Uses:
- **WiFi Access Points**: Maps WiFi networks to known locations
- **Network Databases**: Google/Mozilla location databases
- **IP Geolocation**: Internet connection location data
- **No Battery Drain**: Unlike GPS, very low power consumption

## Current Status
- ✅ Weather API working (19°C fetched successfully)
- ❌ Device location disabled (`GPS enabled: false, Network enabled: false`)
- ✅ Fallback to default coordinates working
- ✅ Content provider attempts logged (TvWeatherClient not accessible)

The weather system is fully functional - it just needs either location services enabled or your hotel's coordinates configured.