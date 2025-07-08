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

package com.karuhun.core.common

import android.util.Log
import com.karuhun.core.model.FoodCategory
import kotlin.coroutines.cancellation.CancellationException

interface Synchronizer {

    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}

private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Log.i(
        "suspendRunCatching",
        "Failed to evaluate a suspendRunCatchingBlock. Returning failure Result",
        exception,
    )
    Result.failure(exception)
}

suspend fun <T> Synchronizer.changeListSync(
    versionReader: suspend () -> Int,
    fetchData: suspend (Int) -> Resource<Pair<List<T>, Int>>,
    saveData: suspend (List<T>) -> Unit,
    updateVersion: suspend (Int) -> Unit,
): Boolean = suspendRunCatching {
    val localVersion = versionReader()
    when (val result = fetchData(localVersion)) {
        is Resource.Success -> {
            val (data, newVersion) = result.data
            saveData(data)
            updateVersion(newVersion)
        }
        is Resource.Error -> throw result.exception
    }
}.isSuccess

suspend fun <T> Synchronizer.forceSyncWithResource(
    fetch: suspend () -> Resource<T>,
    save: suspend (T) -> Unit,
) = suspendRunCatching {
    when (val result = fetch()) {
        is Resource.Success -> save(result.data)
        is Resource.Error -> throw result.exception
    }
}.isSuccess

suspend fun <T> Synchronizer.syncData(
    versionReader: suspend () -> Int,
    fetchData: suspend (Int) -> List<T>,
    saveData: suspend (List<T>) -> Unit,
    updateVersion: suspend (Int) -> Unit,
): Boolean = suspendRunCatching {
    val localVersion = versionReader()
    val data = fetchData(localVersion)

    if (data.isNotEmpty()) {
        saveData(data)
        // Increment version after successful sync
        updateVersion(localVersion + 1)
    }
}.isSuccess

