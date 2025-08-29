package com.karuhun.feature.hotelprofile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karuhun.core.domain.usecase.GetHotelProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelProfileViewModel @Inject constructor(
    private val getHotelProfileUseCase: GetHotelProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HotelProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getHotelProfileUseCase().collect { hp ->
                _uiState.value = HotelProfileUiState(hotelProfile = hp)
            }
        }
    }
}
