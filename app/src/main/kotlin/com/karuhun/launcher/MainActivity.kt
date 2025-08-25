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
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.karuhun.core.common.util.DeviceUtil
import com.karuhun.core.ui.navigation.extension.collectWithLifecycle
import com.karuhun.launcher.core.designsystem.component.RunningText
import com.karuhun.launcher.core.designsystem.component.TopBar
import com.karuhun.launcher.core.designsystem.theme.AppTheme
import com.karuhun.navigation.LauncherAppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var wifiApiService: com.karuhun.core.network.service.WifiApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                var showScreenSaver by remember { mutableStateOf(true) }
                val appState = rememberAppState()
                val viewModel = hiltViewModel<MainViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val uiEffect = viewModel.uiEffect
                val onAction = viewModel::onAction

                // Wifi UI state
                val coroutineScope = rememberCoroutineScope()
                var showWifiDialog by remember { mutableStateOf(false) }
                var wifiList by remember { mutableStateOf<List<com.karuhun.core.model.Wifi>>(emptyList()) }
                var wifiLoading by remember { mutableStateOf(false) }
                val localContext = LocalContext.current
                val wifiViewModel: WifiViewModel = hiltViewModel()
                val wifiListState by wifiViewModel.wifiList.collectAsState()
                val wifiLoadingState by wifiViewModel.isLoading.collectAsState()
                val closeFocusRequester = remember { FocusRequester() }
                var ignoreWifiClicks by remember { mutableStateOf(false) }

//                if (showScreenSaver) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .focusable(true)
//                            .clickable(true, onClick = {
//                                showScreenSaver = false
//                            }),
//                    ){
//                        Text(
//                            text = "Screen Saver",
//                            modifier = Modifier
//                                .align(Alignment.Center)
//                                .fillMaxWidth(),
//                            color = Color.Black,
//                        )
//                    }
//                } else {
                    // Main Launcher Application
                    LauncherApplication(
                        modifier = Modifier.fillMaxSize(),
                        appState = appState,
                        uiState = uiState,
                        uiEffect = uiEffect,
                        onAction = onAction,
                        onMenuItemClick = { menuItem ->
                            if (menuItem.equals("YOUTUBE", ignoreCase = true)) {
                                // Try YouTube Android TV package first, then mobile YouTube
                                val pkgNames = listOf("com.google.android.youtube.tv", "com.google.android.youtube")
                                var launchIntent: android.content.Intent? = null
                                for (pkg in pkgNames) {
                                    launchIntent = packageManager.getLaunchIntentForPackage(pkg)
                                    if (launchIntent != null) break
                                }

                                if (launchIntent != null) {
                                    startActivity(launchIntent)
                                } else {
                                    // Fallback: try a youtube URI, then web URL
                                    try {
                                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                            data = android.net.Uri.parse("youtube://")
                                            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                                        }
                                        startActivity(intent)
                                    } catch (e: Exception) {
                                        val webIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://www.youtube.com")).apply {
                                            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                                        }
                                        startActivity(webIntent)
                                    }
                                }
                            } else if (menuItem.equals("WIFI", ignoreCase = true)) {
                                if (ignoreWifiClicks) {
                                    Log.d("MainActivity", "Ignored WIFI click because of debounce")
                                } else {
                                    Log.d("MainActivity", "WIFI menu pressed")
                                    Toast.makeText(localContext, "Loading Wi‑Fi...", Toast.LENGTH_SHORT).show()
                                    wifiViewModel.loadWifi()
                                }
                            }
                        },
                    )
//                }

                // show full-screen Wi‑Fi dialog when loading OR when wifi list available
                if (wifiLoadingState || wifiListState.isNotEmpty()) {
                    // request focus for Close when overlay opens
                    LaunchedEffect(wifiLoadingState, wifiListState.size) {
                        // start a short-lived coroutine to delay and then request focus
                        coroutineScope.launch {
                            // small delay to ensure composable mounted
                            kotlinx.coroutines.delay(80)
                            try {
                                closeFocusRequester.requestFocus()
                            } catch (_: Exception) { }
                        }
                    }
                     // Full-screen overlay
                     Box(
                         modifier = Modifier
                             .fillMaxSize()
                             .background(Color.Black.copy(alpha = 0.85f)),
                     ) {
                         Column(
                             modifier = Modifier
                                 .fillMaxSize()
                                 .padding(24.dp)
                         ) {
                             // Header with title and close button
                             Box(modifier = Modifier.fillMaxWidth()) {
                                 androidx.tv.material3.Text(
                                     text = "Wi‑Fi Credentials",
                                     modifier = Modifier.align(Alignment.CenterStart),
                                     color = Color.White
                                 )
                                 TextButton(
                                     onClick = {
                                        // close and debounce further wifi clicks
                                        wifiViewModel.clear()
                                        ignoreWifiClicks = true
                                        coroutineScope.launch {
                                            kotlinx.coroutines.delay(800)
                                            ignoreWifiClicks = false
                                        }
                                     },
                                     modifier = Modifier
                                         .align(Alignment.CenterEnd)
                                         .focusRequester(closeFocusRequester)
                                 ) {
                                     androidx.tv.material3.Text(text = "Close", color = Color.White)
                                 }
                             }

                             Spacer(modifier = Modifier.height(12.dp))

                             if (wifiLoadingState) {
                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = Color.White)
                                }
                             }

                             if (wifiListState.isNotEmpty()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
                                        items(wifiListState) { wifi ->
                                            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                                                androidx.tv.material3.Text(text = "SSID: ${wifi.ssid_name}", color = Color.White)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                androidx.tv.material3.Text(text = "Password: ${wifi.ssid_password}", color = Color.White)
                                            }
                                        }
                                    }
                                }
                             } else if (!wifiLoadingState) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    androidx.tv.material3.Text(text = "No Wi‑Fi info configured", color = Color.White)
                                }
                             }
                         }
                     }
                }

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
        AsyncImage(
            model = uiState.hotelProfile?.backgroundPhoto,
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
                guestName = uiState.roomDetail?.guestName.orEmpty(),
                roomNumber = DeviceUtil.getDeviceName(LocalContext.current),
                date = "06 April 2020",
                temperature = "30°C",
                imageUrl = uiState.hotelProfile?.logoWhite.orEmpty(),
                settingsPassword = uiState.hotelProfile?.passwordSetting
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
                    onMenuItemClick = onMenuItemClick,
                )
            }

        }
        RunningText(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp)
                .fillMaxWidth(),
            text = uiState.hotelProfile?.runningText.orEmpty(),
        )
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
