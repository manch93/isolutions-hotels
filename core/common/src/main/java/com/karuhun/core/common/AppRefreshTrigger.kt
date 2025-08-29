/*
 * Copyright 2025 The Android Open Source Project
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

package com.karuhun.core.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Global refresh trigger object that can be used to coordinate data refresh
 * across different ViewModels without dependency injection complexity
 */
object AppRefreshTrigger {
    private val _refreshTrigger = MutableSharedFlow<RefreshEvent>(
        replay = 1, // Keep the last event for late subscribers
        extraBufferCapacity = 1
    )
    val refreshTrigger: SharedFlow<RefreshEvent> = _refreshTrigger.asSharedFlow()

    fun triggerRefresh(reason: String = "Unknown") {
        // android.util.Log.d("AppRefreshTrigger", "Triggering app refresh - reason: $reason")
        _refreshTrigger.tryEmit(RefreshEvent(reason, System.currentTimeMillis()))
    }
}

data class RefreshEvent(
    val reason: String,
    val timestamp: Long
)