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

package com.karuhun.core.network.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = runBlocking { "eyJpdiI6IjJ6dngwVVZWaWxlU2RTb0xiWDR1a3c9PSIsInZhbHVlIjoic2xFTHdlakl2SzdJWDZDb3dBV3YyamxpQmJqWlFMQXo3dTRIRG0rRXJ5K2J4VW9ubkhLV2FkYzdrdHF3bzJRWWh1U3FsV1M0Tmcra0N2anAxdnBvU0h4eTdUV2hxSE1DZTgxOUg0Y0dEVVd2eXhFaGc4WVV0ZHltSSs3NjNvLzFSL2pHa1RvdzJjK1hBaXFJL0pWMG54b0N3cFpVZE1admswVzVtNzJxRWRjc09HU25FeEFSQ0FuUmlHNlI4N09FR2FSZjUxUTlkK1FhUVpjeGt4dlJwdnhWbmwxL3BqNjltQ1pXYTVNQzJCT2pOQWw4Nll6NHNDUmxPdWlUYzYzcVdGSEcvaXUzdVZ3VWtQTTlzcGFoSE5Hd1BaejZLQS9wV3lvVE9Zc0dkOGZYTWgwR3VxOFAwZWcraVpoamVHL0ciLCJtYWMiOiIxZDY5ZTQ5MGZmZjFmOTZkNDc5ZmRlNmZiNWNhNzE2MjIxOTU5MDliZTZmNDAzZWI2NmY0Zjg4NzJiMDUwYWExIiwidGFnIjoiIn0=" }

        val requestBuilder = originalRequest.newBuilder()
        token?.let {
            requestBuilder.addHeader("X-API-KEY", "$it")
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}