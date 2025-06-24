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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme
import kotlinx.coroutines.CoroutineScope
import com.karuhun.launcher.core.designsystem.component.BottomBar
import com.karuhun.launcher.core.designsystem.component.LauncherCard
import com.karuhun.launcher.core.designsystem.component.MenuItemCard
import com.karuhun.launcher.core.designsystem.component.TopBar
import com.karuhun.launcher.core.designsystem.icon.MoreSvgrepoCom
import com.karuhun.launcher.core.designsystem.icon.TvRounded
import com.karuhun.launcher.core.designsystem.icon.WifiSvgrepoCom
import com.karuhun.launcher.core.designsystem.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val appState = rememberAppState()
                LauncherApplication(
                    modifier = Modifier
                        .fillMaxSize(),
                    appState = appState,
                    onMenuItemClick = {},
                )
            }
        }
    }
}

@Composable
fun LauncherApplication(
    modifier: Modifier = Modifier,
    appState: LauncherAppState,
    onMenuItemClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
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
                    .padding(24.dp),
        ) {
            TopBar(
                modifier = Modifier
                    .height(80.dp),
                guestName = "Mr. Bruce Wayne",
                roomNumber = "111",
                date = "06 April 2020",
                temperature = "30°C",
                time = "11:49 AM",
            )
            Row (
                modifier = Modifier
                    .weight(1f),
            ) {
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
            BottomBar(text = "Buy two biriyani and get one free / Eat for free in clontarf")
        }
    }
}

@Composable
@Preview(device = TV_1080p)
fun LauncherApplicationPreview() {
    val navController = rememberNavController()
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    val appState = LauncherAppState(navController = navController, coroutineScope = coroutineScope)
    LauncherApplication(
        modifier = Modifier.fillMaxSize(),
        appState = appState,
        onMenuItemClick = {},
    )
}

data class MenuItem(
    val title: String,
    val icon: ImageVector,
)

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onMenuItemClick: (String) -> Unit
) {

    val menuItems = listOf(
        MenuItem("Hotel Services", Icons.Default.RoomService),
        MenuItem("Concierge", Icons.Default.Person),
        MenuItem("Restaurants & Bars", Icons.Default.Restaurant),
        MenuItem("Wellness & Spa", Icons.Default.Spa),
        MenuItem("Cast to Screen", Icons.Default.Tv),
        MenuItem("Music", Icons.Default.MusicNote),
        MenuItem("TV", Icons.Default.Tv),
        MenuItem("Room Control", Icons.Default.SettingsRemote),
        MenuItem("In-Room Dining", Icons.Default.RoomService),
        MenuItem("Attractions", Icons.Default.Star),
        MenuItem("Feedback", Icons.Default.Feedback),
        MenuItem("View Bill", Icons.Default.Receipt),
        MenuItem("Cart", Icons.Default.ShoppingCart),
        MenuItem("Messages", Icons.Default.Email),
    )

    val homeMenuItems = listOf(
        MenuItem("TV", TvRounded),
        MenuItem("WIFI", WifiSvgrepoCom),
        MenuItem("YOUTUBE", TvRounded),
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd
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
                    painter = painterResource(R.drawable.promo_2),
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
fun HomeContentPreview() {
    HomeContent(onMenuItemClick = {})
}

// Card untuk promo
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PromoItemCard(imageRes: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .height(140.dp),
    ) {
        // Menggunakan AsyncImage dari Coil lebih disarankan untuk memuat dari URL
//        AsyncImage(
//            model = imageRes,
//            contentDescription = "Promotion",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop,
//            placeholder = painterResource(id = R.drawable.placeholder_image) // Gambar placeholder
//        )
    }
}

@Preview
@Composable
fun PromoItemCardPreview() {
    PromoItemCard(imageRes = R.drawable.promo_food, onClick = {})
}