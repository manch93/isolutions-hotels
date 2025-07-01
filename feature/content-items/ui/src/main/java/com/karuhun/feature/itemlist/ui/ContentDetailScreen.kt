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

package com.karuhun.feature.itemlist.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.karuhun.launcher.core.designsystem.component.LauncherCard
import com.karuhun.launcher.core.designsystem.theme.AppTheme

@Composable
fun ContentDetailScreen(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LauncherCard(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .focusable(enabled = false)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("http://cms.fortytv.id:2021/storage/arounds/arounds_25-06-2025_685bcfc1f0628.jpg")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        LauncherCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .focusable(enabled = false)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Mount Bromo",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "The Bromo, or Mount Bromo is an active somma volcano, a Hindu pilgrimage site, and part of the Tengger mountains, in East Java, Indonesia. At 2,329 meters it is not the highest peak of the massif, but is the most active and famous.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Light
                    )
                )
            }
        }
    }
}

@Preview(device = TV_1080p)
@Composable
private fun ItemDetailScreenPreview() {
    AppTheme {
        ContentDetailScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}