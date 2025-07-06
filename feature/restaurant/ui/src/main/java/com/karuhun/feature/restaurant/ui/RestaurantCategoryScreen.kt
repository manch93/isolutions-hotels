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

package com.karuhun.feature.restaurant.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.DrawerState
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.NavigationDrawerItemDefaults
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState

@Composable
internal fun RestaurantCategoryScreen(
    modifier: Modifier = Modifier
) {

    var selectedIndex by remember { mutableIntStateOf(0) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

    val items =
        listOf(
            CategoryItem(
                id = 1,
                name = "Makanan"
            ),
            CategoryItem(
                id = 2,
                name = "Minuman"
            ),
            CategoryItem(
                id = 3,
                name = "Alcohol"
            ),
            CategoryItem(
                id = 4,
                name = "Breakfast"
            ),
            CategoryItem(
                id = 5,
                name = "Dessert"
            ),
            CategoryItem(
                id = 6,
                name = "Appetizer"
            ),
        )

    Box{
        NavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Column(
                    Modifier.background(Color.Transparent).fillMaxHeight().padding(12.dp).selectableGroup(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        val (text, icon) = item

                        NavigationDrawerItem(

                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            modifier = Modifier.onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    selectedIndex = index
                                }
                            },
                            leadingContent = {},
                            colors = NavigationDrawerItemDefaults.colors(
                                containerColor = Color.Black.copy(alpha = 0.60f),
                                focusedContainerColor = Color.Black.copy(alpha = 0.60f),
                                selectedContainerColor = Color.Black.copy(alpha = 0.60f),
                                focusedContentColor = Color.White,
                                contentColor = Color.White,
                                selectedContentColor = Color.White
                            ),
                            shape = NavigationDrawerItemDefaults.shape(
                                shape = RoundedCornerShape(30)
                            ),
                            border = NavigationDrawerItemDefaults.border(
                                focusedBorder = Border(
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )

                            )
                        ) {
                            Text(
                                text = items[index].name
                            )
                        }
                    }
                }

            },
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Selected : ${items[selectedIndex].name}, Focused :",
                    color = Color.White
                )
            }
        }
    }
}

data class CategoryItem(
    val id: Int,
    val name: String
)