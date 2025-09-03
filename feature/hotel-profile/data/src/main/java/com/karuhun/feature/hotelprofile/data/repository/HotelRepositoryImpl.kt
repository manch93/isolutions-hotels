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
import com.karuhun.core.common.map
import com.karuhun.core.common.orZero
import com.karuhun.core.common.toModel
import com.karuhun.core.common.util.DeviceUtil
import com.karuhun.core.data.Synchronizer
import com.karuhun.core.data.syncSimpleData
import com.karuhun.core.database.dao.HotelDao
import com.karuhun.core.database.model.toDomain
import com.karuhun.core.datastore.HotelProfile
import com.karuhun.core.datastore.LauncherPreferencesDatastore
import com.karuhun.core.domain.repository.HotelRepository
import com.karuhun.core.model.Hotel
import com.karuhun.core.model.Promo
import com.karuhun.core.model.RoomDetail
import com.karuhun.core.network.safeApiCall
import com.karuhun.feature.hotelprofile.data.source.HotelApiService
import com.karuhun.feature.hotelprofile.data.source.remote.response.toDomain
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HotelRepositoryImpl @Inject constructor(
    private val api : HotelApiService,
    @ApplicationContext private val context: Context,
    private val launcherDatastore: LauncherPreferencesDatastore
) : HotelRepository{
    override suspend fun getHotelProfile(): Flow<HotelProfile> = launcherDatastore.hotelData
        .map { hotel ->
            HotelProfile(
                id = hotel.id,
                name = hotel.name,
                phone = hotel.phone,
                email = hotel.email,
                website = hotel.website,
                defaultGreeting = hotel.defaultGreeting,
                passwordSetting = hotel.passwordSetting,
                logoWhite = hotel.logoWhite,
                logoBlack = hotel.logoBlack,
                primaryColor = hotel.primaryColor,
                mainPhoto = hotel.mainPhoto,
                backgroundPhoto = hotel.backgroundPhoto,
                introVideo = hotel.introVideo,
                welcomeText = hotel.welcomeText,
                runningText = hotel.runningText,
                instagramUsername = hotel.instagramUsername,
                facebookUsername = hotel.facebookUsername
            )
        }
        .catch {
            emit(HotelProfile.Empty)
        }

    override suspend fun getRoomDetail(): Resource<RoomDetail> {
        return safeApiCall { api.getRoomDetail(DeviceUtil.getDeviceName(context)) }.map { response ->
            // If the room response includes hotel profile data, update the datastore
            response.data?.hotel?.let { hotel ->
                val profile = hotel.profile
                // Update hotel data if there are social media fields, default greeting, or welcome text
                if (!profile?.instagramUsername.isNullOrBlank() || 
                    !profile?.facebookUsername.isNullOrBlank() || 
                    !hotel.defaultGreeting.isNullOrBlank() ||
                    !profile?.welcomeText.isNullOrBlank()) {
                    launcherDatastore.updateHotelData {
                        copy(
                            defaultGreeting = hotel.defaultGreeting ?: defaultGreeting,
                            welcomeText = profile?.welcomeText ?: welcomeText,
                            instagramUsername = profile?.instagramUsername ?: instagramUsername,
                            facebookUsername = profile?.facebookUsername ?: facebookUsername
                        )
                    }
                }
            }
            
            response.data?.toDomain() ?: RoomDetail.Empty
        }
    }

    override suspend fun getPromos(): Resource<List<Promo>> {
        return safeApiCall { api.getPromos() }.map { response ->
            response.data?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.syncSimpleData(
            fetchData = {
                val hotelProfile = safeApiCall { api.getHotelProfile() }
                hotelProfile.toModel()?.data.toDomain()
            },
            saveData = { hotel ->
                launcherDatastore.updateHotelData {
                    copy(
                        id = hotel.id.orZero(),
                        name = hotel.name.orEmpty(),
                        phone = hotel.phone.orEmpty(),
                        email = hotel.email.orEmpty(),
                        website = hotel.website.orEmpty(),
                        defaultGreeting = hotel.defaultGreeting.orEmpty(),
                        passwordSetting = hotel.passwordSetting.orEmpty(),
                        logoWhite = hotel.logoWhite.orEmpty(),
                        logoBlack = hotel.logoBlack.orEmpty(),
                        primaryColor = hotel.primaryColor.orEmpty(),
                        mainPhoto = hotel.mainPhoto.orEmpty(),
                        backgroundPhoto = hotel.backgroundPhoto.orEmpty(),
                        introVideo = hotel.introVideo.orEmpty(),
                        welcomeText = hotel.welcomeText.orEmpty(),
                        runningText = hotel.runningText.orEmpty(),
                        instagramUsername = hotel.instagramUsername.orEmpty(),
                        facebookUsername = hotel.facebookUsername.orEmpty()
                    )
                }

            }
        )
    }
}