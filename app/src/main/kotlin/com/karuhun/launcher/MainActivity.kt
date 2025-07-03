/*
 * Copyright 2025 The Android Open Source Project
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

package com.karuhun.launcher

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.karuhun.launcher.core.designsystem.component.RunningText
import com.karuhun.launcher.core.designsystem.component.TopBar
import com.karuhun.launcher.core.designsystem.theme.AppTheme
import com.karuhun.navigation.LauncherAppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val appState = rememberAppState()
                val viewModel = hiltViewModel<MainViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val uiEffect = viewModel.uiEffect
                val onAction = viewModel::onAction

                LauncherApplication(
                    modifier = Modifier
                        .fillMaxSize(),
                    appState = appState,
                    onMenuItemClick = {},
                    uiState = uiState,
                    uiEffect = uiEffect,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
fun LauncherApplication(
    modifier: Modifier = Modifier,
    appState: LauncherAppState,
    uiState: MainContract.UiState,
    uiEffect: Flow<MainContract.UiEffect>,
    onAction: (MainContract.UiAction) -> Unit,
    onMenuItemClick: (String) -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.hotel_room_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        top = 24.dp,
                        bottom = 48.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),
        ) {
            TopBar(
                modifier = Modifier
                    .height(80.dp),
                guestName = "Mr. Adolf Stalin",
                roomNumber = "111",
                date = "06 April 2020",
                temperature = "30°C",
                time = "11:49 AM",
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        bottom = 12.dp,
                    ),
            ) {
                LauncherAppNavGraph(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = appState.navController,
                )
            }

        }
        if (uiState.hotelProfile?.runningText != null){
            RunningText(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
                    .fillMaxWidth(),
                text = "uiState.hotelProfile.runningText!!",
            )
        }
    }
}

@Composable
@Preview(device = TV_1080p)
fun LauncherApplicationPreview() {
    val navController = rememberNavController()
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    val viewModel = hiltViewModel<MainViewModel>()
    val appState = LauncherAppState(
        navController = navController,
        coroutineScope = coroutineScope,
        viewModel = viewModel,
    )
    LauncherApplication(
        modifier = Modifier.fillMaxSize(),
        appState = appState,
        onMenuItemClick = {},
        uiState = MainContract.UiState(),
        uiEffect = emptyFlow(),
        onAction = {},
    )
}