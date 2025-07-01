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

import com.karuhun.core.common.Resource
import com.karuhun.core.common.Synchronizer
import com.karuhun.core.common.map
import com.karuhun.core.domain.repository.HotelRepository
import com.karuhun.core.model.Hotel
import com.karuhun.core.network.safeApiCall
import com.karuhun.feature.hotelprofile.data.source.HotelApiService
import com.karuhun.feature.hotelprofile.data.source.remote.response.toDomain
import javax.inject.Inject

class HotelRepositoryImpl @Inject constructor(
    private val api : HotelApiService
) : HotelRepository{
    override suspend fun getHotelProfile(): Resource<Hotel> {
        return safeApiCall { api.getHotelProfile() }.map { response ->
            response.data.toDomain()
        }
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return false
    }
}