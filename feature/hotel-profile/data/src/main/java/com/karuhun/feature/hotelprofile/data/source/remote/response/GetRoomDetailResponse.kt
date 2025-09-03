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
import com.karuhun.core.model.RoomDetail

data class GetRoomDetailResponse(

	@field:SerializedName("guest_name")
	val guestName: String? = null,

	@field:SerializedName("greeting")
	val greeting: String? = null,

	@field:SerializedName("is_birthday")
	val isBirthday: Int? = null,

	@field:SerializedName("hotel")
	val hotel: HotelInRoom? = null
)

data class HotelInRoom(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("default_greeting")
	val defaultGreeting: String? = null,

	@field:SerializedName("password_setting")
	val passwordSetting: String? = null,

	@field:SerializedName("profile")
	val profile: ProfileInRoom? = null
)

data class ProfileInRoom(
	@field:SerializedName("logo_white")
	val logoWhite: String? = null,

	@field:SerializedName("logo_black")
	val logoBlack: String? = null,

	@field:SerializedName("primary_color")
	val primaryColor: String? = null,

	@field:SerializedName("main_photo")
	val mainPhoto: String? = null,

	@field:SerializedName("background_photo")
	val backgroundPhoto: String? = null,

	@field:SerializedName("intro_video")
	val introVideo: String? = null,

	@field:SerializedName("welcome_text")
	val welcomeText: String? = null,

	@field:SerializedName("running_text")
	val runningText: String? = null,

	@field:SerializedName("instagram_username")
	val instagramUsername: String? = null,

	@field:SerializedName("facebook_username")
	val facebookUsername: String? = null
)

fun GetRoomDetailResponse?.toDomain() = RoomDetail(
    guestName = this?.guestName.orEmpty(),
    greeting = this?.greeting,
    isBirthday = this?.isBirthday?.let { it == 1 }
)