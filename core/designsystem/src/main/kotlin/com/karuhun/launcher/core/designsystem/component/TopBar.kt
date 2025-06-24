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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.karuhun.launcher.core.designsystem.icon.HiltonHotelsLogo
import com.karuhun.launcher.core.designsystem.theme.AppTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    guestName: String,
    roomNumber: String,
    date: String,
    temperature: String,
    time: String,
) {
    Row(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        ), // Tambahkan padding luar jika perlu
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Bagian Logo dan Welcome Message
        Row(
            modifier = Modifier.height(IntrinsicSize.Min), // Kunci: Membuat tinggi Row ini sesuai dengan anak tertinggi (logo)
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Logo
            Image(
                imageVector = HiltonHotelsLogo,
                contentDescription = "Logo", // Selalu berikan contentDescription
                modifier = Modifier
                    .size(64.dp), // Tentukan ukuran logo jika belum ada ukuran intrinsik yang pas
                // atau .height(64.dp) jika hanya ingin mengontrol tinggi
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Welcome Message
            Column(
                modifier = Modifier
                    .fillMaxHeight(), // Kunci: Kolom teks akan mengisi tinggi Row (yang ditentukan oleh logo)
                verticalArrangement = Arrangement.SpaceAround, // Atau Arrangement.Center, SpaceEvenly, dll.
                // atau gunakan weight pada Text di bawah
            ) {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = Color.White,
                )
                Text(
                    text = guestName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        shadow = Shadow(offset = Offset(x = 1f, y = 1f), blurRadius = 1f)
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        // Spacer untuk mendorong sisa item ke kanan
        Spacer(modifier = Modifier.weight(1f))

        // Info Kanan (tetap sama)
        Text(
            text = "Room $roomNumber",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterVertically), // Pastikan alignment
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = date, color = Color.White, fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterVertically))
        Spacer(modifier = Modifier.width(24.dp))
        Icon(
            Icons.Default.WbSunny,
            contentDescription = "Weather",
            tint = Color.Yellow,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = temperature, color = Color.White, fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterVertically))
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = time,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
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
            guestName = "John Doe",
            roomNumber = "101",
            date = "July 26, 2024",
            temperature = "25°C",
            time = "10:00 AM",
        )
    }
}

