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

package com.karuhun.feature.screensaver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.karuhun.core.ui.navigation.extension.collectWithLifecycle
import com.karuhun.feature.screensaver.ui.component.VideoPlayer
import kotlinx.coroutines.flow.Flow

@Composable
fun ScreenSaver(
    modifier: Modifier = Modifier,
    uiState: ScreenSaverContract.UiState,
    uiEffect: Flow<ScreenSaverContract.UiEffect>,
    onAction: (ScreenSaverContract.UiAction) -> Unit
) {
    // Handle UI effects
    uiEffect.collectWithLifecycle{ effect ->
        when (effect) {
            is ScreenSaverContract.UiEffect.ShowError -> {

            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Video Background
        uiState.videoConfig?.let { videoConfig ->
            VideoPlayer(
                modifier = Modifier.fillMaxSize(),
                videoUri = uiState.hotelProfile?.introVideo.orEmpty(),
                isPlaying = uiState.isVideoPlaying,
                isMuted = videoConfig.isMuted,
                onError = { errorMessage ->
                    onAction(ScreenSaverContract.UiAction.OnVideoError(errorMessage))
                }
            )
        }

        // Hotel Information Overlay
        if (!uiState.isLoading && uiState.hotelProfile != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontSize = 24.sp
                )
                Text(
                    text = uiState.hotelProfile.name.orEmpty(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Loading Indicator
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
    }
}