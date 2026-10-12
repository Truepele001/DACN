package com.bitchat.android.dashboard

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DACNDashboardServerTest {

    @Test
    fun testDashboardServerEndpoints() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val server = DACNDashboardServer.startServer(context, 8080)
        assertNotNull(server)
        assertTrue(server.isAlive)

        DACNDashboardServer.stopServer()
    }
}
