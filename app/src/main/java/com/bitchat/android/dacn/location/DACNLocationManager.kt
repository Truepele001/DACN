package com.bitchat.android.dacn.location

import android.content.Context
import android.location.Location
import android.util.Log
import com.bitchat.android.dacn.identity.DACNIdentityManager
import com.bitchat.android.geohash.FusedLocationProvider
import com.bitchat.android.geohash.Geohash
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Represents a DACN Location Payload for peer-to-peer location sharing over the mesh.
 */
data class DACNLocationPayload(
    val senderPeerID: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val geohash: String,
    val accuracyMeters: Float,
    val timestampMs: Long
)

/**
 * Privacy control levels for geohash location sharing precision.
 */
enum class LocationPrecisionLevel(val geohashLength: Int) {
    EXACT(8),        // ~19m x 19m
    NEIGHBORHOOD(6), // ~1.2km x 0.6km
    CITY(4)          // ~39km x 19km
}

/**
 * Manages location retrieval, geohash encoding, and location sharing payloads for DACN.
 */
object DACNLocationManager {

    private const val TAG = "DACNLocationManager"
    private val gson = Gson()

    private val _currentLocationPayload = MutableStateFlow<DACNLocationPayload?>(null)
    val currentLocationPayload: StateFlow<DACNLocationPayload?> = _currentLocationPayload.asStateFlow()

    private var fusedLocationProvider: FusedLocationProvider? = null

    @Synchronized
    fun initialize(context: Context) {
        if (fusedLocationProvider == null) {
            fusedLocationProvider = FusedLocationProvider(context.applicationContext)
            DACNIdentityManager.initialize(context)
        }
    }

    /**
     * Captures current device location and formats a DACNLocationPayload.
     */
    fun getCurrentLocation(
        context: Context,
        precision: LocationPrecisionLevel = LocationPrecisionLevel.NEIGHBORHOOD,
        onLocationRetrieved: (DACNLocationPayload?) -> Unit
    ) {
        initialize(context)
        val provider = fusedLocationProvider ?: FusedLocationProvider(context.applicationContext)

        provider.getLastKnownLocation { location ->
            if (location != null) {
                val payload = buildLocationPayload(context, location, precision)
                _currentLocationPayload.value = payload
                onLocationRetrieved(payload)
            } else {
                provider.requestFreshLocation { freshLocation ->
                    if (freshLocation != null) {
                        val payload = buildLocationPayload(context, freshLocation, precision)
                        _currentLocationPayload.value = payload
                        onLocationRetrieved(payload)
                    } else {
                        Log.w(TAG, "Failed to retrieve location from provider.")
                        onLocationRetrieved(null)
                    }
                }
            }
        }
    }

    /**
     * Builds a DACNLocationPayload using a Location object and precision preference.
     */
    fun buildLocationPayload(
        context: Context,
        location: Location,
        precision: LocationPrecisionLevel = LocationPrecisionLevel.NEIGHBORHOOD
    ): DACNLocationPayload {
        DACNIdentityManager.initialize(context)
        val peerID = DACNIdentityManager.getPeerID()
        val displayName = DACNIdentityManager.getDisplayName()

        val fullGeohash = Geohash.encode(location.latitude, location.longitude, precision.geohashLength)

        return DACNLocationPayload(
            senderPeerID = peerID,
            displayName = displayName,
            latitude = location.latitude,
            longitude = location.longitude,
            geohash = fullGeohash,
            accuracyMeters = location.accuracy,
            timestampMs = System.currentTimeMillis()
        )
    }

    /**
     * Serializes location payload to JSON string for mesh transmission.
     */
    fun serializeLocationPayload(payload: DACNLocationPayload): String {
        return gson.toJson(payload)
    }

    /**
     * Deserializes JSON string back to DACNLocationPayload.
     */
    fun deserializeLocationPayload(json: String): DACNLocationPayload? {
        return try {
            gson.fromJson(json, DACNLocationPayload::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to deserialize location payload", e)
            null
        }
    }
}
