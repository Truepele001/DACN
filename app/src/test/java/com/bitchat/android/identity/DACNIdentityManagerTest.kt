package com.bitchat.android.identity

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DACNIdentityManagerTest {

    @Test
    fun testIdentityGenerationAndPersistence() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val identityManager = DACNIdentityManager.getInstance(context)

        val pubKey = identityManager.getPublicKey()
        val privKey = identityManager.getPrivateKey()
        val peerID = identityManager.getPeerID()
        val displayName = identityManager.getDisplayName()

        assertNotNull(pubKey)
        assertEquals(32, pubKey.size)
        assertNotNull(privKey)
        assertEquals(32, privKey.size)
        assertNotNull(peerID)
        assertEquals(64, peerID.length) // Hex string of 32 bytes = 64 chars
        assertEquals("DACN User", displayName)

        // Test display name persistence
        identityManager.setDisplayName("Alice DACN")
        assertEquals("Alice DACN", identityManager.getDisplayName())
    }
}
