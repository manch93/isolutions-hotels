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

package com.karuhun.feature.hotelprofile.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.karuhun.core.model.Promo

data class GetPromosResponse(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("hotel_id")
	val hotelId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null
)

fun GetPromosResponse?.toDomain() = Promo(
    id = this?.id ?: 0,
    hotelId = this?.hotelId ?: 0,
    name = this?.name.orEmpty(),
    description = this?.description.orEmpty(),
    image = this?.image.orEmpty()
)