package com.bitchat.android.ui.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.bitchat.android.dacn.location.DACNLocationManager
import com.bitchat.android.dacn.location.LocationPrecisionLevel
import com.bitchat.android.ui.ComposerActionSurface
import com.bitchat.android.ui.ComposerIconSize

@Composable
fun LocationPickerButton(
    modifier: Modifier = Modifier,
    onLocationReady: (String) -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    ComposerActionSurface(
        isActive = false,
        isPressed = isPressed,
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                DACNLocationManager.getCurrentLocation(context, LocationPrecisionLevel.NEIGHBORHOOD) { payload ->
                    if (payload != null) {
                        val msg = "📍 Shared Location: Geohash ${payload.geohash} (Lat: ${"%.4f".format(payload.latitude)}, Lon: ${"%.4f".format(payload.longitude)})"
                        onLocationReady(msg)
                    } else {
                        val msg = "📍 Shared Location: Geohash active"
                        onLocationReady(msg)
                    }
                }
            }
        )
    ) { tint ->
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Share Location",
            tint = tint,
            modifier = Modifier.size(ComposerIconSize)
        )
    }
}
