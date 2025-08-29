/*
 * Copyright 2025 The Karuhun Developer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karuhun.feature.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karuhun.core.common.onFailure
import com.karuhun.core.common.onSuccess
import com.karuhun.core.data.util.SyncManager
import com.karuhun.core.domain.usecase.GetHotelProfileUseCase
import com.karuhun.core.domain.usecase.GetRoomDetailUseCase
import com.karuhun.core.ui.navigation.delegate.mvi.MVI
import com.karuhun.core.ui.navigation.delegate.mvi.mvi
import com.karuhun.core.common.AppRefreshTrigger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getHotelProfileUseCase: GetHotelProfileUseCase,
    private val getRoomDetailUseCase: GetRoomDetailUseCase,
) : ViewModel(),
    MVI<HomeContract.UiState, HomeContract.UiAction, HomeContract.UiEffect> by mvi(initialState = HomeContract.UiState()) {

    init {
        // android.util.Log.d("HomeViewModel", "HomeViewModel initialized")
        onAction(HomeContract.UiAction.LoadMenuItems)
        onAction(HomeContract.UiAction.LoadRoomDetail)
        
        // Listen for refresh triggers from MainActivity
        // android.util.Log.d("HomeViewModel", "Setting up refresh trigger listener")
        viewModelScope.launch {
            // android.util.Log.d("HomeViewModel", "Started collecting refresh triggers")
            AppRefreshTrigger.refreshTrigger.collect { event ->
                // android.util.Log.d("HomeViewModel", "Received refresh trigger: ${event.reason} at ${event.timestamp}")
                // Refresh all data when triggered
                refreshAllData()
            }
        }
    }
    override fun onAction(action: HomeContract.UiAction) {
        when (action) {
            HomeContract.UiAction.LoadMenuItems -> {
                loadMenuItems()
            }

            HomeContract.UiAction.OnMenuItemClick -> {}
            HomeContract.UiAction.OnMoreClick -> {}
            HomeContract.UiAction.LoadRoomDetail -> { loadRoomDetail() }
        }
    }

    private fun loadMenuItems() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getHotelProfileUseCase().collect { hotelProfile ->
            updateUiState {
                copy(
                    isLoading = false,
                    hotelProfile = hotelProfile,
                )
            }
        }
    }

    private fun loadRoomDetail() = viewModelScope.launch {
        getRoomDetailUseCase()
            .onSuccess {
                updateUiState {
                    copy(
                        roomDetail = it
                    )
                }
            }
            .onFailure { error ->
                Log.e("HomeViewModel", "Failed to load room detail: $error")
            }
    }

    private fun refreshAllData() = viewModelScope.launch {
        // Log.d("HomeViewModel", "Refreshing all data...")
        // Refresh both hotel profile and room detail
        onAction(HomeContract.UiAction.LoadMenuItems)
        onAction(HomeContract.UiAction.LoadRoomDetail)
    }
}