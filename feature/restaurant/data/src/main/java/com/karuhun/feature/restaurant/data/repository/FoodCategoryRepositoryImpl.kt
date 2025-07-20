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

import com.karuhun.core.data.Synchronizer
import com.karuhun.core.data.syncData
import com.karuhun.core.database.dao.FoodCategoryDao
import com.karuhun.core.database.model.toDomainList
import com.karuhun.core.database.model.toEntityList
import com.karuhun.core.datastore.LauncherPreferencesDatastore
import com.karuhun.core.domain.repository.FoodCategoryRepository
import com.karuhun.core.model.FoodCategory
import com.karuhun.feature.restaurant.data.source.remote.RestaurantNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FoodCategoryRepositoryImpl @Inject constructor(
    private val foodCategoryDao: FoodCategoryDao,
    private val launcherDatastore: LauncherPreferencesDatastore,
    private val restaurantNetworkDataSource: RestaurantNetworkDataSource
) : FoodCategoryRepository{
    override fun getRestaurantCategories(): Flow<List<FoodCategory>?> {
        return foodCategoryDao.getAll().map {
            it.toDomainList()
        }
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.syncData(
            versionReader = { launcherDatastore.versionData.first().foodCategoryVersion },
            fetchData = { version ->
                restaurantNetworkDataSource.getFoodCategories(version)
            },
            saveData = { data ->
                foodCategoryDao.upsert(data.toEntityList())
            },
            updateVersion = { newVersion ->
                launcherDatastore.setFoodCategoryVersion(newVersion)
            }
        )
//        return synchronizer.syncData(
//            versionReader = { launcherDatastore.versionData.first().foodCategoryVersion },
//            fetchFoodCategories = { version ->
//                restaurantNetworkDataSource.getFoodCategories(version)
//            },
//            saveFoodCategories = { foodCategories ->
//                foodCategoryDao.upsert(foodCategories.toEntityList())
//            },
//            updateVersion = { newVersion ->
//                launcherDatastore.setFoodCategoryVersion(newVersion)
//            }
//        )
    }
}