package com.bitchat.android.dacn.location

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitchat.android.ui.theme.BitchatFontFamily
import kotlin.math.cos
import kotlin.math.sin

/**
 * Embedded Offline Map & Peer Radar Sheet for DACN.
 * Renders offline peer finding radar, real-time distance/bearing telemetry, and launches offline map intents.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DACNOfflineMapSheet(
    peerName: String,
    peerLat: Double,
    peerLon: Double,
    myLat: Double,
    myLon: Double,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val peerFinding = remember(peerLat, peerLon, myLat, myLon) {
        DACNOfflineMapEngine.calculatePeerFinding(myLat, myLon, peerLat, peerLon)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        scrimColor = Color.Black.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DACN Offline Peer Radar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BitchatFontFamily
                    )
                    Text(
                        text = "100% Offline Direction & Distance Finder",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Offline Compass Radar View
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(Color.Black.copy(alpha = 0.85f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val maxRadius = size.width / 2 - 16.dp.toPx()

                    // Draw concentric offline range rings
                    drawCircle(Color.Green.copy(alpha = 0.15f), maxRadius * 0.33f, center, style = Stroke(1.5f))
                    drawCircle(Color.Green.copy(alpha = 0.25f), maxRadius * 0.66f, center, style = Stroke(1.5f))
                    drawCircle(Color.Green.copy(alpha = 0.35f), maxRadius, center, style = Stroke(2f))

                    // Draw crosshairs
                    drawLine(Color.Green.copy(alpha = 0.2f), Offset(center.x, 16.dp.toPx()), Offset(center.x, size.height - 16.dp.toPx()), strokeWidth = 1f)
                    drawLine(Color.Green.copy(alpha = 0.2f), Offset(16.dp.toPx(), center.y), Offset(size.width - 16.dp.toPx(), center.y), strokeWidth = 1f)

                    // Draw You (center green beacon)
                    drawCircle(Color(0xFF32D74B), 8.dp.toPx(), center)
                    drawCircle(Color(0xFF32D74B).copy(alpha = 0.3f), 16.dp.toPx(), center)

                    // Plot Peer location relative to bearing
                    val bearingRad = Math.toRadians((peerFinding.initialBearingDegrees - 90).toDouble())
                    val peerDistFraction = (peerFinding.distanceMeters / 1000f).coerceIn(0.2f, 0.9f)
                    val peerRadius = maxRadius * peerDistFraction
                    val peerX = center.x + (peerRadius * cos(bearingRad)).toFloat()
                    val peerY = center.y + (peerRadius * sin(bearingRad)).toFloat()

                    // Draw Peer (red beacon)
                    drawCircle(Color(0xFFFF453A), 10.dp.toPx(), Offset(peerX, peerY))
                    drawCircle(Color(0xFFFF453A).copy(alpha = 0.4f), 18.dp.toPx(), Offset(peerX, peerY))
                }
            }

            Spacer(Modifier.height(20.dp))

            // Telemetry Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Navigation, contentDescription = null, tint = Color(0xFF32D74B))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = peerName,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Text(
                            text = DACNOfflineMapEngine.formatDistance(peerFinding.distanceMeters),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF32D74B),
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Heading: ${peerFinding.initialBearingDegrees.toInt()}° (${peerFinding.cardinalDirection})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Peer Coordinates: ${"%.4f".format(peerLat)}, ${"%.4f".format(peerLon)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // External Offline Maps Button
            Button(
                onClick = {
                    openExternalOfflineMapApp(context, peerLat, peerLon, peerName)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(imageVector = Icons.Filled.Map, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Open in Offline Maps (OsmAnd / Organic Maps)", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

private fun openExternalOfflineMapApp(context: Context, lat: Double, lon: Double, label: String) {
    val geoUri = "geo:$lat,$lon?q=$lat,$lon(${Uri.encode(label)})".toUri()
    val intent = Intent(Intent.ACTION_VIEW, geoUri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
