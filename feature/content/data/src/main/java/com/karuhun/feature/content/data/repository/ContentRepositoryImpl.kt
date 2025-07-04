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

package com.karuhun.feature.content.data.repository

import android.util.Log
import com.karuhun.core.common.Resource
import com.karuhun.core.common.Synchronizer
import com.karuhun.core.common.forceSyncWithResource
import com.karuhun.core.common.onSuccess
import com.karuhun.core.common.orZero
import com.karuhun.core.common.toModel
import com.karuhun.core.database.dao.ContentDao
import com.karuhun.core.database.dao.ContentItemDao
import com.karuhun.core.database.model.toEntity
import com.karuhun.core.database.model.toModel
import com.karuhun.core.domain.repository.ContentRepository
import com.karuhun.core.model.Content
import com.karuhun.core.network.safeApiCall
import com.karuhun.feature.content.data.source.ContentApiService
import com.karuhun.feature.content.data.source.remote.response.toDomain
import com.karuhun.feature.content.data.source.remote.response.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val apiService: ContentApiService,
    private val contentDao: ContentDao,
    private val contentItemDao: ContentItemDao
) : ContentRepository {
    override suspend fun getContents(): Flow<List<Content>> {
        return contentDao.getAll().map { it.toModel() }
    }

    override suspend fun getContentItemById(id: String): Content? {
        return contentDao.getContentById(id.toInt())?.toModel()
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.forceSyncWithResource(
            fetch = {
                safeApiCall { apiService.getContentItems() }
            },
            save = { response ->
                val contents = response.data.toDomain()
                contentDao.deleteAll()
                contentDao.upsert(contents.toEntity())
                contents.forEach { content ->
                    when (val itemResult = safeApiCall { apiService.getContentItemById(content.id.toString()) }) {
                        is Resource.Success -> {
                            contentItemDao.upsert(itemResult.data.data.toDomainModel().toEntity())
                        }
                        is Resource.Error -> {
                            // Log error tapi tidak throw exception untuk item individual
                            Log.w("ContentRepository", "Failed to sync content item ${content.id}: ${itemResult.exception.message}")
                        }
                    }
                }
            }
        )
    }
}