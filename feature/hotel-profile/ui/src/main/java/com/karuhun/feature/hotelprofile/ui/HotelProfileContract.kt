package com.karuhun.feature.hotelprofile.ui

import com.karuhun.core.datastore.HotelProfile

data class HotelProfileUiState(
    val hotelProfile: HotelProfile? = HotelProfile.Empty,
)
