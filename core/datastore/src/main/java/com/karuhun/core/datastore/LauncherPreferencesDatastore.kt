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

package com.karuhun.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.karuhun.core.model.VersionData
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LauncherPreferencesDatastore @Inject constructor(
    private val version: DataStore<Version>
) {
    val versionData = version.data
        .map {
            VersionData(
                foodCategoryVersion = it.foodCategoryVersion,
                foodVersion = it.foodVersion
            )
        }

    suspend fun getVersion() = versionData.collect {
        it.foodCategoryVersion
    }

    suspend fun setFoodCategoryVersion(newVersion: Int) {
        try {
            version.updateData {
                it.copy {
                    foodCategoryVersion = newVersion
                }
            }
        } catch (ioException: IOException) {
            Log.d("LauncherPreferences", "Failed to update food category version")
        }
    }
    suspend fun setFoodVersion(newVersion: Int) {
        try {
            version.updateData {
                it.copy {
                    foodVersion = newVersion
                }
            }
        } catch (ioException: IOException) {
            Log.d("LauncherPreferences", "Failed to update food category version")
        }
    }

    suspend fun getChangeListVersions(): ChangeListVersions = version.data
        .map {
            ChangeListVersions(
                foodVersion = it.foodVersion,
                foodCategoryVersion = it.foodCategoryVersion,
                applicationVersion = it.applicationVersion
            )
        }
        .firstOrNull() ?: ChangeListVersions()

    suspend fun updateChangeListVersions(
        update: ChangeListVersions.() -> ChangeListVersions
    ) {
        try {
            version.updateData { currentPreferences ->
                val updatedPreferences = update(
                    ChangeListVersions(
                        foodVersion = currentPreferences.foodVersion,
                        foodCategoryVersion = currentPreferences.foodCategoryVersion,
                        applicationVersion = currentPreferences.applicationVersion
                    )
                )
                currentPreferences.copy {
                    foodVersion = updatedPreferences.foodVersion
                    foodCategoryVersion = updatedPreferences.foodCategoryVersion
                }
            }
        } catch (ioException: IOException) {
            Log.e("LauncherPreferences", "Failed to update change list versions", ioException)
        }
    }
}