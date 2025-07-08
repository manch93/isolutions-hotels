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

package com.karuhun.feature.restaurant.data.source.remote

import com.karuhun.core.model.FoodCategory
import com.karuhun.feature.restaurant.data.source.RestaurantApiService
import com.karuhun.feature.restaurant.data.source.remote.response.toDomainList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantNetworkDataSource @Inject constructor(
    private val restaurantApiService: RestaurantApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getFoodCategories(after: Int): List<FoodCategory> {
        return withContext(ioDispatcher) {
            val allFoodCategories = mutableListOf<FoodCategory>()
            var currentPage = 1


            do {
                val params = mapOf(
                    "after" to after.toString(),
                    "paginate" to "10",
                    "page" to "$currentPage"
                )
                val response = restaurantApiService.getCategories(params)
                allFoodCategories.addAll(response.data?.data?.toDomainList() ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allFoodCategories
        }
    }
}