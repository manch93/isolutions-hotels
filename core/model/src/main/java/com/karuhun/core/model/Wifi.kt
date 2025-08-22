package com.karuhun.core.model

/**
 * Wi‑Fi model returned by the backend
 */
data class Wifi(
    val id: Int,
    val hotel_id: Int,
    val ssid_name: String,
    val ssid_password: String,
    val security_type: String,
)
