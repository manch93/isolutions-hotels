package com.karuhun.feature.hotelprofile.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.karuhun.core.ui.navigation.Screen
import com.karuhun.feature.hotelprofile.ui.HotelProfileScreen
import com.karuhun.feature.hotelprofile.ui.HotelProfileViewModel
import kotlinx.serialization.Serializable

@Serializable data object HotelProfile : Screen

fun NavGraphBuilder.hotelProfileScreen(
    onBack: () -> Unit,
) {
    composable<HotelProfile> {
        val viewModel = hiltViewModel<HotelProfileViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        HotelProfileScreen(hotelProfile = uiState.hotelProfile, onBack = onBack)
    }
}
