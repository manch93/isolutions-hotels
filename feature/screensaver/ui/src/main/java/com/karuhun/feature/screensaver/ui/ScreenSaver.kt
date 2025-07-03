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
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.Text
import kotlinx.coroutines.flow.Flow

@Composable
fun ScreenSaver(
    modifier: Modifier = Modifier,
    uiState: ScreenSaverContract.UiState,
    uiEffect: Flow<ScreenSaverContract.UiEffect>,
    onAction: (ScreenSaverContract.UiAction) -> Unit
) {
    Box(
        modifier = modifier.background(Color.White)
    ) {
        Text(
            text = uiState.hotelProfile?.name.orEmpty(),
            modifier = modifier
        )
    }
}