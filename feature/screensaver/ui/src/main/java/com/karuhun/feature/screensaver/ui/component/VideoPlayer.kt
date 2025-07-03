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

package com.karuhun.feature.screensaver.ui.component

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoUri: String,
    isPlaying: Boolean = true,
    isMuted: Boolean = false,
    onPlayerReady: (ExoPlayer) -> Unit = {},
    onError: (String) -> Unit = {},
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        createExoPlayer(
            context = context,
            onError = onError,
        )
    }

    LaunchedEffect(videoUri) {
        if (videoUri.isNotEmpty()) {
            try {
                val mediaItem = MediaItem.fromUri(videoUri)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                onPlayerReady(exoPlayer)
            } catch (e: Exception) {
                onError("Failed to load video: ${e.message}")
            }
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    LaunchedEffect(isMuted) {
        exoPlayer.volume = if (isMuted) 0f else 1f
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = false // Hide controls for screensaver
                setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
            }
        },
    )

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}

private fun createExoPlayer(
    context: Context,
    onError: (String) -> Unit,
): ExoPlayer {
    return ExoPlayer.Builder(context)
        .build()
        .apply {
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true

            addListener(
                object : Player.Listener {
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        super.onPlayerError(error)
                        onError("Playback error: ${error.message}")
                    }
                },
            )
        }
}
