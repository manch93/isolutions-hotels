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

package com.karuhun.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karuhun.feature.home.ui.model.MenuItem
import com.karuhun.launcher.core.designsystem.component.LauncherCard
import com.karuhun.launcher.core.designsystem.component.MenuItemCard
import com.karuhun.launcher.core.designsystem.icon.MoreSvgrepoCom
import com.karuhun.launcher.core.designsystem.theme.AppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMenuItemClick: (String) -> Unit = { _ -> }
) {
    Row {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            content = {},
        )
        HomeContent(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            onMenuItemClick = onMenuItemClick,
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onMenuItemClick: (String) -> Unit
) {
    val homeMenuItems = MenuItem.items

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .width(320.dp)
        ) {
            LauncherCard (
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(start = 8.dp, end = 8.dp),
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(com.karuhun.launcher.core.designsystem.R.drawable.promo_2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 8.dp,
                ),
            ) {

                // Menampilkan item menu biasa
                items(homeMenuItems.size) { index ->
                    MenuItemCard(
                        title = homeMenuItems[index].title,
                        icon = homeMenuItems[index].icon,
                        onClick = { onMenuItemClick(homeMenuItems[index].title) },
                    )
                }
            }
            MenuItemCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 8.dp, end = 8.dp),
                icon = MoreSvgrepoCom,
                onClick = { onMenuItemClick("") },
            )
        }

    }
}

@Preview(device = TV_1080p)
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            onMenuItemClick = {}
        )
    }
}