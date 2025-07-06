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

package com.karuhun.feature.restaurant.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.karuhun.core.common.BasePagingSource.Companion.DEFAULT_PAGE_SIZE
import com.karuhun.core.common.Resource
import com.karuhun.core.common.Synchronizer
import com.karuhun.core.common.forceSyncWithResource
import com.karuhun.core.database.dao.FoodCategoryDao
import com.karuhun.core.database.model.FoodCategoryEntity
import com.karuhun.core.database.model.toDomainList
import com.karuhun.core.domain.repository.FoodCategoryRepository
import com.karuhun.core.model.FoodCategory
import com.karuhun.feature.restaurant.data.paging.FoodCategoryPagingSource
import com.karuhun.feature.restaurant.data.source.RestaurantApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class FoodCategoryRepositoryImpl @Inject constructor(
    private val restaurantApiService: RestaurantApiService,
    private val foodCategoryDao: FoodCategoryDao
) : FoodCategoryRepository{
    override fun getRestaurantCategories(): Flow<List<FoodCategory>?> {
        return foodCategoryDao.getAll().map {
            it.toDomainList()
        }
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.forceSyncWithResource(
            fetch = {
                val allCategories = mutableListOf<FoodCategoryEntity>()

                val pagingSource = FoodCategoryPagingSource(restaurantApiService)
                var currentPage = 1
                var hasNextPage = true

                while (hasNextPage) {
                    val result = pagingSource.fetchData(currentPage, DEFAULT_PAGE_SIZE)
                    if (result.isEmpty() || result.size < DEFAULT_PAGE_SIZE) {
                        hasNextPage = false
                    } else {
                        currentPage++
                    }

                    val entities = pagingSource.mapToLocalData(result)
                    allCategories.addAll(entities)
                }

                Resource.Success(allCategories)
            },
            save = { categories ->

                foodCategoryDao.deleteAll()
                foodCategoryDao.upsert(categories)
            }
        )
    }
}