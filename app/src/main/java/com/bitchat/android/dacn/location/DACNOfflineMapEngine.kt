package com.bitchat.android.dacn.location

import android.location.Location
import kotlin.math.*

/**
 * Peer distance and directional heading result for 100% offline peer finding.
 */
data class PeerDistanceBearing(
    val distanceMeters: Float,
    val initialBearingDegrees: Float,
    val cardinalDirection: String
)

/**
 * Offline Map & Peer Finding Engine for DACN.
 * Computes distance, bearing angles, and compass headings completely offline without internet or cellular connection.
 */
object DACNOfflineMapEngine {

    /**
     * Calculates offline distance in meters, bearing angle, and cardinal direction between two locations.
     */
    fun calculatePeerFinding(
        myLat: Double,
        myLon: Double,
        peerLat: Double,
        peerLon: Double
    ): PeerDistanceBearing {
        var distance = 0f
        var bearing = 0f

        try {
            val results = FloatArray(2)
            Location.distanceBetween(myLat, myLon, peerLat, peerLon, results)
            distance = results[0]
            bearing = results[1]
        } catch (_: Throwable) {
            val earthRadius = 6371000.0
            val dLat = Math.toRadians(peerLat - myLat)
            val dLon = Math.toRadians(peerLon - myLon)
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(myLat)) * cos(Math.toRadians(peerLat)) *
                    sin(dLon / 2) * sin(dLon / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            distance = (earthRadius * c).toFloat()

            val y = sin(dLon) * cos(Math.toRadians(peerLat))
            val x = cos(Math.toRadians(myLat)) * sin(Math.toRadians(peerLat)) -
                    sin(Math.toRadians(myLat)) * cos(Math.toRadians(peerLat)) * cos(dLon)
            bearing = (Math.toDegrees(atan2(y, x)).toFloat() + 360f) % 360f
        }

        if (bearing < 0) {
            bearing += 360f
        }

        val cardinal = getCardinalDirection(bearing)
        return PeerDistanceBearing(
            distanceMeters = distance,
            initialBearingDegrees = bearing,
            cardinalDirection = cardinal
        )
    }

    /**
     * Formats distance in meters or kilometers cleanly for offline UI display.
     */
    fun formatDistance(meters: Float): String {
        return if (meters < 1000f) {
            "${meters.roundToInt()} m"
        } else {
            "%.2f km".format(meters / 1000f)
        }
    }

    private fun getCardinalDirection(bearingDegrees: Float): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW", "N")
        val index = ((bearingDegrees + 22.5f) % 360f / 45f).toInt()
        return directions[index]
    }
}
