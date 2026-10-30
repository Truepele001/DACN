package com.bitchat.android.location

import android.location.Location
import androidx.test.core.app.ApplicationProvider
import com.bitchat.android.dacn.location.DACNLocationManager
import com.bitchat.android.dacn.location.LocationPrecisionLevel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DACNLocationManagerTest {

    @Test
    fun testLocationPayloadBuildAndSerialization() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        val mockLocation = Location("test_provider").apply {
            latitude = 37.7749
            longitude = -122.4194
            accuracy = 5.0f
        }

        val payload = DACNLocationManager.buildLocationPayload(
            context = context,
            location = mockLocation,
            precision = LocationPrecisionLevel.NEIGHBORHOOD
        )

        assertNotNull(payload)
        assertEquals(37.7749, payload.latitude, 0.0001)
        assertEquals(-122.4194, payload.longitude, 0.0001)
        assertEquals(6, payload.geohash.length) // NEIGHBORHOOD length = 6
        assertEquals(5.0f, payload.accuracyMeters)

        // Test JSON Serialization & Deserialization
        val json = DACNLocationManager.serializeLocationPayload(payload)
        assertNotNull(json)
        assertTrue(json.contains("geohash"))

        val deserialized = DACNLocationManager.deserializeLocationPayload(json)
        assertNotNull(deserialized)
        assertEquals(payload.senderPeerID, deserialized?.senderPeerID)
        assertEquals(payload.geohash, deserialized?.geohash)
        assertEquals(payload.latitude, deserialized?.latitude ?: 0.0, 0.0001)
    }
}
