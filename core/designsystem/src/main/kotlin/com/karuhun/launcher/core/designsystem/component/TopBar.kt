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

package com.karuhun.launcher.core.designsystem.component

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.ui.graphics.vector.ImageVector
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.foundation.focusable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.Key
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Surface
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import coil.compose.AsyncImage
import com.karuhun.launcher.core.designsystem.theme.AppTheme

// Function to get appropriate weather icon based on weather code
private fun getWeatherIcon(weatherCode: Int): ImageVector {
    return when (weatherCode) {
        0 -> Icons.Default.WbSunny // Clear Sky
        1, 2, 3 -> Icons.Default.CloudQueue // Partly Cloudy
        45, 48 -> Icons.Default.Cloud // Foggy
        51, 53, 55 -> Icons.Default.WaterDrop // Light Rain
        56, 57 -> Icons.Default.WaterDrop // Freezing Rain
        61, 63, 65 -> Icons.Default.WaterDrop // Rain
        66, 67 -> Icons.Default.WaterDrop // Freezing Rain
        71, 73, 75 -> Icons.Default.AcUnit // Snow
        77 -> Icons.Default.AcUnit // Snow Grains
        80, 81, 82 -> Icons.Default.WaterDrop // Rain Showers
        85, 86 -> Icons.Default.AcUnit // Snow Showers
        95 -> Icons.Default.Thunderstorm // Thunderstorm
        96, 99 -> Icons.Default.Thunderstorm // Thunderstorm with Hail
        else -> Icons.Default.WbSunny // Default to Clear
    }
}

// Function to get appropriate weather icon color
private fun getWeatherIconColor(weatherCode: Int): Color {
    return when (weatherCode) {
        0 -> Color.Yellow // Clear Sky - Sun
        1, 2, 3 -> Color(0xFFB0BEC5) // Partly Cloudy - Light Gray
        45, 48 -> Color(0xFF90A4AE) // Foggy - Gray
        51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> Color(0xFF42A5F5) // Rain - Blue
        71, 73, 75, 77, 85, 86 -> Color(0xFFE3F2FD) // Snow - Light Blue/White
        95, 96, 99 -> Color(0xFF7E57C2) // Thunderstorm - Purple
        else -> Color.Yellow // Default to Yellow
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    guestName: String,
    roomNumber: String,
    date: String,
    temperature: String,
    weatherCode: Int = 0, // Default to clear sky
    imageUrl: String,
    settingsPassword: String? = null,
    onOpenSettings: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Logo",
                modifier = Modifier
                    .size(64.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

//            Column(
//                modifier = Modifier
//                    .fillMaxHeight(),
//                verticalArrangement = Arrangement.spacedBy(2.dp),
//            ) {
//                Text(
//                    text = "Welcome",
//                    //style = MaterialTheme.typography.headlineSmall.copy(
                // Text(
                //     text = "Room $roomNumber",
                //     color = Color(0xFFEFEFEF),
                //     fontSize = 18.sp,
                //     modifier = Modifier.align(Alignment.CenterVertically),
                // )
//                    text = guestName,
//                    style = MaterialTheme.typography.headlineMedium.copy(
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp,
//                    ),
//                    color = Color.White,
//                )
//            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Room $roomNumber",
            color = Color(0xFFEFEFEF),
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = date, color = Color(0xFFEFEFEF), fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterVertically))
        Spacer(modifier = Modifier.width(24.dp))
        Icon(
            getWeatherIcon(weatherCode),
            contentDescription = "Weather",
            tint = getWeatherIconColor(weatherCode),
            modifier = Modifier.align(Alignment.CenterVertically),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = temperature, color = Color(0xFFEFEFEF), fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterVertically))
        Spacer(modifier = Modifier.width(24.dp))
        // Settings icon: TV-first focus/click handling with visual feedback
        Spacer(modifier = Modifier.width(16.dp))
        val _context = LocalContext.current
        var showPassDialog by remember { mutableStateOf(false) }
        var inputPass by remember { mutableStateOf("") }

        // We use LauncherCard for consistent focus visuals with other menu items

        // Encapsulate the action to open settings so we can call from key, click or tests
        val openSettingsAction = {
            if (settingsPassword.isNullOrBlank()) {
                try {
                    val intent = Intent(Settings.ACTION_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    // prefer callback for tests/dev preview
                    onOpenSettings?.invoke() ?: _context.startActivity(intent)
                } catch (_: Exception) { }
            } else {
                showPassDialog = true
            }
        }

        // Use LauncherCard to match the orange focused outline and scale used elsewhere
        LauncherCard(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(56.dp),
            onClick = { openSettingsAction() },
            radius = 10.dp,
            // Make the card background transparent so only the border is visible
            color = CardDefaults.colors(
                containerColor = Color.Transparent,
                focusedContentColor = Color.White,
                contentColor = Color.White,
                pressedContentColor = Color.White
            ),
            borderColor = MaterialTheme.colorScheme.primary,
            // thinner border so the settings icon can appear slightly larger
            borderWidth = 1.dp
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color(0xFFEFEFEF),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        TextClock(
            modifier = Modifier.align(Alignment.CenterVertically),
        )

        // Password dialog (custom-styled Dialog to match app look)
        if (showPassDialog) {
            Dialog(onDismissRequest = { showPassDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    // slightly transparent surface so underlying app content peeks through
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Enter password to open Settings",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        androidx.compose.material3.OutlinedTextField(
                            value = inputPass,
                            onValueChange = { inputPass = it },
                            label = { androidx.compose.material3.Text("Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                showPassDialog = false
                                inputPass = ""
                            }) {
                                androidx.compose.material3.Text("Cancel", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f))
                            }

                            TextButton(onClick = {
                                if (inputPass == settingsPassword) {
                                    try {
                                        val intent = Intent(Settings.ACTION_SETTINGS).apply {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        }
                                        onOpenSettings?.invoke() ?: _context.startActivity(intent)
                                    } catch (_: Exception) { }
                                    showPassDialog = false
                                    inputPass = ""
                                } else {
                                    android.widget.Toast.makeText(_context, "Incorrect password", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                androidx.compose.material3.Text("Open", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.95f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(device = TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TopBarPreview() {
    AppTheme {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            guestName = "Mr. John Doe",
            roomNumber = "101",
            date = "July 26, 2024",
            temperature = "25°C",
            weatherCode = 61, // Rain for testing
            imageUrl = "",
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(device = TV_1080p, showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TopBarPasswordPreview() {
    var opened by remember { mutableStateOf(false) }
    AppTheme {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            guestName = "Mrs. Tester",
            roomNumber = "202",
            date = "Aug 25, 2025",
            temperature = "22°C",
            weatherCode = 71, // Snow for testing
            imageUrl = "",
            settingsPassword = "123456",
            onOpenSettings = { opened = true }
        )
        androidx.compose.material3.Text(
            text = if (opened) "Settings opened" else "",
            color = Color.White,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

