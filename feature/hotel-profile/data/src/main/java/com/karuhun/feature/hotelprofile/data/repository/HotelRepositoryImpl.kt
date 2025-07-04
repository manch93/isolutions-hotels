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

package com.karuhun.feature.hotelprofile.data.repository

import android.content.Context
import com.karuhun.core.common.Resource
import com.karuhun.core.common.Synchronizer
import com.karuhun.core.common.forceSync
import com.karuhun.core.common.map
import com.karuhun.core.common.toModel
import com.karuhun.core.database.dao.HotelDao
import com.karuhun.core.database.model.toDomain
import com.karuhun.core.database.model.toEntity
import com.karuhun.core.domain.repository.HotelRepository
import com.karuhun.core.model.Hotel
import com.karuhun.core.model.RoomDetail
import com.karuhun.core.network.safeApiCall
import com.karuhun.feature.hotelprofile.data.source.HotelApiService
import com.karuhun.feature.hotelprofile.data.source.remote.response.toDomain
import com.karuhun.core.common.util.DeviceUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HotelRepositoryImpl @Inject constructor(
    private val api : HotelApiService,
    private val hotelDao: HotelDao,
    @ApplicationContext private val context: Context
) : HotelRepository{
    override suspend fun getHotelProfile(): Flow<Hotel> {
        return hotelDao.getHotelProfile().map { it.toDomain() }
    }

    override suspend fun getRoomDetail(): Resource<RoomDetail> {
        return safeApiCall { api.getRoomDetail(DeviceUtil.getDeviceName(context)) }.map {
            it.data.toDomain()
        }
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean = synchronizer.forceSync(
        fetch = {
            val response = safeApiCall { api.getHotelProfile() }.toModel()
            response?.data.toDomain()
        },
        save = { hotel ->
            hotelDao.deleteAll()
            hotelDao.upsert(hotel.toEntity())
        },
    )
}