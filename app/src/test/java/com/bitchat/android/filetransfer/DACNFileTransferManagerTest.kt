package com.bitchat.android.filetransfer

import androidx.test.core.app.ApplicationProvider
import org.bouncycastle.crypto.generators.X25519KeyPairGenerator
import org.bouncycastle.crypto.params.X25519KeyGenerationParameters
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters
import org.bouncycastle.crypto.params.X25519PublicKeyParameters
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.security.SecureRandom

@RunWith(RobolectricTestRunner::class)
class DACNFileTransferManagerTest {

    @Test
    fun testFileEncryptionChunkingAndReceipt() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val fileTransferManager = DACNFileTransferManager.getInstance(context)

        val keyGen = X25519KeyPairGenerator()
        keyGen.init(X25519KeyGenerationParameters(SecureRandom()))
        val recipientKeyPair = keyGen.generateKeyPair()
        val recipientPrivateKey = (recipientKeyPair.private as X25519PrivateKeyParameters).encoded
        val recipientPublicKey = (recipientKeyPair.public as X25519PublicKeyParameters).encoded

        val tempFile = File.createTempFile("dacn_test_", ".txt", context.cacheDir)
        val testContent = "Hello, DACN Encrypted File Transfer over BLE Mesh!".repeat(50)
        tempFile.writeText(testContent)

        // 1. Send / Encrypt & Chunk
        val chunks = fileTransferManager.sendFile(tempFile, recipientPublicKey)
        assertTrue(chunks.isNotEmpty())

        for (chunk in chunks) {
            assertEquals(chunks.first().transferId, chunk.transferId)
            assertEquals(chunks.size, chunk.totalChunks)
            assertTrue(chunk.data.size <= 512)
        }

        // 2. Receive & Decrypt & Verify
        val receivedFile = fileTransferManager.receiveFile(chunks, recipientPrivateKey)
        assertNotNull(receivedFile)
        assertTrue(receivedFile.exists())
        assertEquals(testContent, receivedFile.readText())

        tempFile.delete()
        receivedFile.delete()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testFileExceedingMaxSizeThrowsException() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val fileTransferManager = DACNFileTransferManager.getInstance(context)

        val mockFile = object : File("mock_large.bin") {
            override fun exists(): Boolean = true
            override fun length(): Long = 6 * 1024 * 1024 // 6MB > 5MB
        }

        fileTransferManager.sendFile(mockFile, ByteArray(32))
    }
}
