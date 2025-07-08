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

import com.karuhun.core.common.BasePagingSource.Companion.DEFAULT_PAGE_SIZE
import com.karuhun.core.common.Resource
import com.karuhun.core.common.Synchronizer
import com.karuhun.core.common.forceSyncWithResource
import com.karuhun.core.common.syncData
import com.karuhun.core.database.dao.FoodDao
import com.karuhun.core.database.model.FoodCategoryEntity
import com.karuhun.core.database.model.FoodEntity
import com.karuhun.core.database.model.toDomainList
import com.karuhun.core.database.model.toEntityList
import com.karuhun.core.datastore.LauncherPreferencesDatastore
import com.karuhun.core.domain.repository.FoodRepository
import com.karuhun.core.model.Food
import com.karuhun.feature.restaurant.data.paging.FoodCategoryPagingSource
import com.karuhun.feature.restaurant.data.paging.FoodPagingSource
import com.karuhun.feature.restaurant.data.source.RestaurantApiService
import com.karuhun.feature.restaurant.data.source.remote.RestaurantNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
    private val restaurantApiService: RestaurantApiService,
    private val networkDataSource: RestaurantNetworkDataSource,
    private val launcherDatastore: LauncherPreferencesDatastore
) : FoodRepository{

    override fun getFoodsByCategoryId(categoryId: Int): Flow<List<Food>> {
        return foodDao.getByCategoryId(categoryId).map { foods ->
            foods.toDomainList()
        }
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.syncData(
            versionReader = {
                launcherDatastore.versionData.first().foodVersion
            },
            fetchData = { networkDataSource.getFoods(it)},
            saveData = { foodList ->
                val foodsDeleted: List<Int> = foodList.filter { it.isDeleted == 1 }
                    .map { it.id }
                val foodsToSave = foodList.filter { it.isDeleted != 1 }.toEntityList()

                foodDao.delete(foodsDeleted)
                foodDao.upsert(foodsToSave)

            },
            updateVersion = {
//                launcherDatastore.setFoodVersion(it)
            }
        )
    }
}