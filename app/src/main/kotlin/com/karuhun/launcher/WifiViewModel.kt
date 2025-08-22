package com.karuhun.launcher

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karuhun.core.model.Wifi
import com.karuhun.core.network.service.WifiApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WifiViewModel @Inject constructor(
    private val wifiApiService: WifiApiService,
) : ViewModel() {
    private val _wifiList = MutableStateFlow<List<Wifi>>(emptyList())
    val wifiList: StateFlow<List<Wifi>> = _wifiList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadWifi() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resp = wifiApiService.getWifi()
                _wifiList.value = resp.data
                Log.d("WifiViewModel", "got wifi: ${'$'}{resp.data.size} entries")
            } catch (e: Exception) {
                Log.e("WifiViewModel", "loadWifi failed", e)
                _wifiList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clear() {
        _wifiList.value = emptyList()
    }
}
