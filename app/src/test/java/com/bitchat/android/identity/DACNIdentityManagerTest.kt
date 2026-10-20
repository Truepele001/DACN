package com.bitchat.android.identity

import androidx.test.core.app.ApplicationProvider
import com.bitchat.android.dacn.identity.DACNIdentityManager
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.security.MessageDigest

@RunWith(RobolectricTestRunner::class)
class DACNIdentityManagerTest {

    @Test
    fun testIdentityManagerInitializationAndPersistence() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        DACNIdentityManager.initialize(context)

        val pubKey = DACNIdentityManager.getPublicKey()
        val privKey = DACNIdentityManager.getPrivateKey()
        val peerID = DACNIdentityManager.getPeerID()
        val pubKeyHex = DACNIdentityManager.getPublicKeyHex()
        val displayName = DACNIdentityManager.getDisplayName()

        assertNotNull(pubKey)
        assertEquals(32, pubKey.size)
        assertNotNull(privKey)
        assertEquals(32, privKey.size)

        // Verify peerID is SHA-256 hash of public key bytes (lowercase hex)
        val expectedHash = MessageDigest.getInstance("SHA-256").digest(pubKey)
        val expectedPeerID = expectedHash.joinToString("") { "%02x".format(it) }.lowercase()

        assertEquals(expectedPeerID, peerID)
        assertEquals(64, pubKeyHex.length)
        assertEquals("DACN User", displayName)

        // Test setting display name
        DACNIdentityManager.setDisplayName(context, "Alice DACN")
        assertEquals("Alice DACN", DACNIdentityManager.getDisplayName())

        // Test non-regeneration on second initialization
        DACNIdentityManager.initialize(context)
        assertEquals(peerID, DACNIdentityManager.getPeerID())
        assertArrayEquals(pubKey, DACNIdentityManager.getPublicKey())
        assertArrayEquals(privKey, DACNIdentityManager.getPrivateKey())
    }
}
