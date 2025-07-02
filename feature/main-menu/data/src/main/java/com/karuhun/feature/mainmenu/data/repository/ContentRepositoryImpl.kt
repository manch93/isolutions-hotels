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

package com.karuhun.feature.mainmenu.data.repository

import com.karuhun.core.common.Synchronizer
import com.karuhun.core.common.forceSync
import com.karuhun.core.common.toModel
import com.karuhun.core.database.dao.ContentDao
import com.karuhun.core.database.model.toEntity
import com.karuhun.core.database.model.toModel
import com.karuhun.core.domain.repository.ContentRepository
import com.karuhun.core.model.ContentItem
import com.karuhun.core.network.safeApiCall
import com.karuhun.feature.mainmenu.data.source.ContentApiService
import com.karuhun.feature.mainmenu.data.source.remote.response.toDomain
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val apiService: ContentApiService,
    private val contentDao: ContentDao
) : ContentRepository {
    override suspend fun getContentItems(): List<ContentItem> {
        return contentDao.getAll().toModel()
    }

    override suspend fun getContentItemById(id: String): ContentItem? {
        return contentDao.getContentById(id.toInt())?.toModel()
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.forceSync(
            fetch = {
                val response = safeApiCall { apiService.getContentItems() }.toModel()
                response?.data.toDomain()
            },
            save = {
                contentDao.deleteAll()
                contentDao.upsert(it.toEntity())
            }
        )
    }
}