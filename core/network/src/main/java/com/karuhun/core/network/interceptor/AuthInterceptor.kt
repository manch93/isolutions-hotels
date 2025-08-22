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

        val token = runBlocking { "eyJpdiI6Ik9ENzhQUmZSSkcrR1gvYUl1NzR0aGc9PSIsInZhbHVlIjoiMDZGaG5ROEZQUjhDOU8xTy9rNEtnQi9PMTRTNEtkaUhhS3BmcHhNYkp6VE9aTXhwRlM0MDFjTzlkMjNGY25IR0hERHlTRlV3eTV5cXJ5cUhlc3VyQ1dPU2o5dXBaVDBxbEVhZ2MzdC9QQ0VWN2YwT3FxQ0VKcmNEaFJWN0laY05BWVBmWW1WckdRN3JXTVlIczZuNDIweGhNNmM3b3czRjZPYWx3MG1teHpLaUhJazZYa0tSR1NYTTV1c09pQWFFem1IOFhYb1lVcnRzZFJVTjlMbldaYkc4RmFzY2RiVUZqMmJJdXBpbjFhQjh6YTVoS3NEMWtJVExuMFJtdXhSSVZ2M1RXV0xQZHVjVWN0b21PL2YyaEoyZzhrME5rYTl3bEMvSkhPdmZRV3FnVElaMG51K3FJeDcrSEcrY2Y5b0xscVNHWm5USEl1UnZRZUVYUDhQS2pRPT0iLCJtYWMiOiI2MzEwZDdmODFjM2Y2MjVjZDI5OWYwNjM3NDEzYzU2MjhlOGFiMjVjNDIzMGEzNGEzN2U2MWNjYzY2MTFiOGI4IiwidGFnIjoiIn0=" }

        val requestBuilder = originalRequest.newBuilder()
        token?.let {
            requestBuilder.addHeader("X-API-KEY", "$it")
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}