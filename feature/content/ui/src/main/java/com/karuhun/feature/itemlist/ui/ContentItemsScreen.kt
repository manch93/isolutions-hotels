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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.karuhun.launcher.core.designsystem.component.LauncherCard
import com.karuhun.launcher.core.designsystem.theme.AppTheme

@Composable
fun ContentItemsScreen(
    modifier: Modifier = Modifier,
) {
    val detailMenuList = DetailMenuItem.detailMenuList

    Column {
        Text(
            text = "Arround Us",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = Color.White,
            modifier = Modifier
                .padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                .fillMaxWidth(),

            )
        LazyHorizontalGrid(
            modifier = modifier,
            rows = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            items(
                count = detailMenuList.size,
                key = { detailMenuList[it].id },
            ) {
                LauncherCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .width(250.dp),
                    onClick = {},
                ) {
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(detailMenuList[it].image)
                                .crossfade(true)
                                .build(),
                            contentDescription = detailMenuList[it].name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        LauncherCard (
                            modifier = Modifier
                                .height(70.dp)
                                .align(Alignment.BottomCenter)
                        ) {
                            Box (
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = detailMenuList[it].name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraLight,
                                        fontSize = 20.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    color = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .padding(8.dp),
                                    maxLines = 2,

                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = TV_1080p)
@Composable
private fun DetailMenuScreenPreview() {
    AppTheme {
        ContentItemsScreen(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

data class DetailMenuItem(
    val id: Int,
    val name: String,
    val image: String,
) {
    companion object {
        val detailMenuList = listOf(
            DetailMenuItem(
                id = 1,
                name = "Bromo Tengger Semeru",
                image = "http://cms.fortytv.id:2021/storage/arounds/arounds_25-06-2025_685bcfc1f0628.jpg",
            ),
            DetailMenuItem(
                id = 2,
                name = "Cheng Ho Mosque Fajar",
                image = "http://cms.fortytv.id:2021/storage/arounds/arounds_25-06-2025_685bcf9bd5ff9.jpg",
            ),
            DetailMenuItem(
                id = 3,
                name = "De Javasche Bank Soerabaia",
                image = "http://cms.fortytv.id:2021/storage/arounds/arounds_25-06-2025_685bcfa7bb563.jpg",
            ),
            DetailMenuItem(
                id = 4,
                name = "Switzerland",
                image = "http://cms.fortytv.id:2021/storage/arounds/arounds_25-06-2025_685bcfafba975.jpg",
            ),
            DetailMenuItem(
                id = 5,
                name = "Bromo Tengger Semeru ",
                image = "http://cms.fortytv.id:2021/storage/arounds/arounds_25-06-2025_685bcfb7a75e1.jpg",
            ),
        )
    }
}