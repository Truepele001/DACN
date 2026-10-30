package com.bitchat.android.location

import com.bitchat.android.dacn.location.DACNOfflineMapEngine
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DACNOfflineMapEngineTest {

    @Test
    fun testOfflineDistanceAndBearingCalculation() {
        val sfLat = 37.7749
        val sfLon = -122.4194
        val oakLat = 37.8044
        val oakLon = -122.2712

        val result = DACNOfflineMapEngine.calculatePeerFinding(sfLat, sfLon, oakLat, oakLon)

        assertNotNull(result)
        assertTrue("Distance should be greater than 0", result.distanceMeters > 0)

        val formattedDistance = DACNOfflineMapEngine.formatDistance(result.distanceMeters)
        assertNotNull(formattedDistance)
    }
}
