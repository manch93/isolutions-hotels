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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tv
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.karuhun.launcher.core.designsystem.icon.TvRounded // Asumsi ini adalah ikon default Anda

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MenuItemCard(
    title: String? = null,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier // Terima modifier dari pemanggil
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .width(90.dp)
            .height(100.dp),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        ),
        scale = CardDefaults.scale(
            focusedScale = 1.05f
        ),
        colors = CardDefaults.colors( // Menggunakan CardDefaults.colors yang lebih umum
            containerColor = Color.Black.copy(alpha = 0.60f),
            focusedContentColor = Color.White,
            contentColor = Color.White // Warna konten default untuk teks dan ikon di dalam card
        ),
        shape = CardDefaults.shape(RoundedCornerShape(12.dp)) // Sedikit mengurangi radius sudut
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 8.dp), // Padding internal untuk konten
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Pusatkan konten secara vertikal
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title, // Content description yang baik untuk aksesibilitas
                modifier = Modifier
                    .size(46.dp), // Ukuran ikon yang cukup besar, bisa disesuaikan
                // tint = Color.White // Tidak perlu jika contentColor pada Card sudah diset
            )

            Spacer(modifier = Modifier.height(6.dp)) // Jarak antara ikon dan teks

            if (title != null) {
                Text(
                    text = title,
                    // color = Color.White, // Tidak perlu jika contentColor pada Card sudah diset
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center, // Pusatkan teks jika lebih dari 1 baris
                    maxLines = 2, // Izinkan hingga 2 baris untuk judul yang panjang
                    overflow = TextOverflow.Ellipsis, // Tambahkan elipsis jika teks terlalu panjang
                    modifier = Modifier.fillMaxWidth() // Agar textAlign.Center berfungsi baik
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF) // Latar belakang preview lebih gelap
@Composable
fun MenuItemCardPreview() {
    MenuItemCard(
        title = "Live TV",
        icon = TvRounded, // Menggunakan ikon yang relevan dari project Anda
        onClick = {}
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF) // Latar belakang preview lebih gelap
@Composable
fun MenuItemCardNoTitlePreview() {
    MenuItemCard(
        icon = TvRounded, // Menggunakan ikon yang relevan dari project Anda
        onClick = {}
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MenuItemCardLongTitlePreview() {
    MenuItemCard(
        title = "Movies & Entertainment", // Judul yang lebih panjang
        icon = Icons.Filled.Tv, // Contoh ikon lain
        onClick = {},
        modifier = Modifier.width(200.dp).height(160.dp) // Contoh override ukuran
    )
}