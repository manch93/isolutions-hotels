package com.karuhun.feature.hotelprofile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
// ...existing code...
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.semantics.Role
import coil.compose.AsyncImage
import com.karuhun.core.datastore.HotelProfile

@Composable
fun HotelProfileScreen(
    hotelProfile: HotelProfile?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hp = hotelProfile ?: HotelProfile.Empty

    // make the host surface transparent so the device wallpaper shows through (wifi portal style)
    Surface(modifier = modifier.fillMaxSize(), color = Color.Transparent) {
        // Center a card that contains the hero image and hotel info
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 1200.dp)
                    .padding(12.dp),
                shape = RoundedCornerShape(12.dp),
                // semi-transparent card so underlying wallpaper is visible (captive/wifi look)
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
            ) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    // Hero image with a subtle gradient scrim and overlaid hotel name
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 140.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))) {
                        AsyncImage(
                            model = hp.backgroundPhoto.orEmpty(),
                            contentDescription = "Hotel background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        )

                        // gradient scrim to improve text contrast
                        Box(modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xAA000000)),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                        )

                        // Hotel name over the image
                        Text(
                            text = hp.name.orEmpty(),
                            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                            modifier = Modifier
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = hp.welcomeText.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )

                    // contact info: phone, email, website (clickable)
                    val context = LocalContext.current
                    if (!hp.phone.isNullOrEmpty()) {
                        Text(
                            text = "Phone: ${hp.phone}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)),
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .clickable(role = Role.Button) {
                                    // open dialer
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${hp.phone}")
                                    }
                                    context.startActivity(intent)
                                }
                        )
                    }

                    if (!hp.email.isNullOrEmpty()) {
                        Text(
                            text = "Email: ${hp.email}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)),
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    if (!hp.website.isNullOrEmpty()) {
                        Text(
                            text = hp.website,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline),
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .clickable(role = Role.Button) {
                                    // open in browser — ensure scheme is present
                                    val raw = hp.website.trim()
                                    val url = if (raw.startsWith("http://") || raw.startsWith("https://")) raw else "https://$raw"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Back button receives initial focus when opened
                    val focusRequester = remember { FocusRequester() }
                    LaunchedEffect(Unit) {
                        // request focus after composition
                        focusRequester.requestFocus()
                    }

                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .focusRequester(focusRequester)
                    ) {
                        Text(text = "Back", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}
