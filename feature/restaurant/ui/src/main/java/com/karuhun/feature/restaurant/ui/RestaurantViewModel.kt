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

import androidx.lifecycle.ViewModel
import com.karuhun.core.domain.usecase.GetFoodCategories
import com.karuhun.core.ui.navigation.delegate.mvi.MVI
import com.karuhun.core.ui.navigation.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class RestaurantViewModel @Inject constructor(
    private val getFoodCategories: GetFoodCategories
) : ViewModel(),
    MVI<RestaurantContract.UiState, RestaurantContract.UiAction, RestaurantContract.UiEffect> by mvi(
        initialState = RestaurantContract.UiState(),
    ) {

    override fun onAction(action: RestaurantContract.UiAction) {
        when(action){
            RestaurantContract.UiAction.LoadCategory -> {}
        }
    }
}