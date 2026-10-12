package com.bitchat.android.filetransfer

import android.content.Context
import com.bitchat.android.identity.DACNIdentityManager
import org.bouncycastle.crypto.agreement.X25519Agreement
import org.bouncycastle.crypto.generators.X25519KeyPairGenerator
import org.bouncycastle.crypto.params.X25519KeyGenerationParameters
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters
import org.bouncycastle.crypto.params.X25519PublicKeyParameters
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Data class representing a 512-byte BLE-compatible file chunk.
 */
data class FileChunk(
    val transferId: String,
    val chunkIndex: Int,
    val totalChunks: Int,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as FileChunk
        return transferId == other.transferId &&
                chunkIndex == other.chunkIndex &&
                totalChunks == other.totalChunks &&
                data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        var result = transferId.hashCode()
        result = 31 * result + chunkIndex
        result = 31 * result + totalChunks
        result = 31 * result + data.contentHashCode()
        return result
    }
}

/**
 * Encrypted File Transfer Manager for DACN.
 * Handles file encryption (AES-256-GCM), key exchange (X25519), chunking (512 bytes), and integrity verification (SHA-256).
 */
class DACNFileTransferManager(private val context: Context) {

    companion object {
        const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024 // 5MB maximum limit
        const val CHUNK_SIZE_BYTES = 512

        @Volatile
        private var instance: DACNFileTransferManager? = null

        fun getInstance(context: Context): DACNFileTransferManager {
            return instance ?: synchronized(this) {
                instance ?: DACNFileTransferManager(context.applicationContext).also { instance = it }
            }
        }
    }

    /**
     * Encrypts file using AES-256-GCM via X25519 key exchange and fragments into 512-byte BLE chunks.
     *
     * @param file File to send (up to 5MB)
     * @param recipientPublicKey 32-byte X25519 public key of recipient
     * @return List of 512-byte FileChunk objects
     */
    fun sendFile(file: File, recipientPublicKey: ByteArray): List<FileChunk> {
        require(file.exists()) { "File does not exist: ${file.absolutePath}" }
        require(file.length() <= MAX_FILE_SIZE_BYTES) {
            "File size (${file.length()} bytes) exceeds maximum limit of 5MB ($MAX_FILE_SIZE_BYTES bytes)"
        }

        val rawFileBytes = file.readBytes()
        val fileHash = MessageDigest.getInstance("SHA-256").digest(rawFileBytes)

        // 1. Generate Ephemeral X25519 KeyPair for Key Exchange
        val keyGen = X25519KeyPairGenerator()
        keyGen.init(X25519KeyGenerationParameters(SecureRandom()))
        val ephemeralKeyPair = keyGen.generateKeyPair()
        val ephemeralPrivateKey = ephemeralKeyPair.private as X25519PrivateKeyParameters
        val ephemeralPublicKey = ephemeralKeyPair.public as X25519PublicKeyParameters
        val ephemeralPubKeyBytes = ephemeralPublicKey.encoded

        // 2. Prepare Recipient X25519 Public Key Parameters
        val recipientPubKeyParams = X25519PublicKeyParameters(ensure32Bytes(recipientPublicKey), 0)

        // 3. Perform X25519 Agreement to derive Shared Secret
        val agreement = X25519Agreement()
        agreement.init(ephemeralPrivateKey)
        val sharedSecret = ByteArray(32)
        agreement.calculateAgreement(recipientPubKeyParams, sharedSecret, 0)

        // Derive AES-256 Key from SHA-256(sharedSecret)
        val aesKeyBytes = MessageDigest.getInstance("SHA-256").digest(sharedSecret)
        val aesKey = SecretKeySpec(aesKeyBytes, "AES")

        // 4. Encrypt file raw bytes with AES-256-GCM
        val iv = ByteArray(12)
        SecureRandom().nextBytes(iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, GCMParameterSpec(128, iv))
        val encryptedPayload = cipher.doFinal(rawFileBytes)

        // 5. Package Header + Encrypted Payload
        // Header: Ephemeral PubKey (32B) + IV (12B) + SHA-256 Hash (32B) = 76 bytes
        val packagedBytes = ByteArray(76 + encryptedPayload.size)
        System.arraycopy(ephemeralPubKeyBytes, 0, packagedBytes, 0, 32)
        System.arraycopy(iv, 0, packagedBytes, 32, 12)
        System.arraycopy(fileHash, 0, packagedBytes, 44, 32)
        System.arraycopy(encryptedPayload, 0, packagedBytes, 76, encryptedPayload.size)

        // 6. Fragment into 512-byte BLE Chunks
        val transferId = UUID.randomUUID().toString().take(8)
        val totalChunks = (packagedBytes.size + CHUNK_SIZE_BYTES - 1) / CHUNK_SIZE_BYTES
        val chunks = mutableListOf<FileChunk>()

        for (i in 0 until totalChunks) {
            val start = i * CHUNK_SIZE_BYTES
            val end = minOf(start + CHUNK_SIZE_BYTES, packagedBytes.size)
            val chunkData = packagedBytes.copyOfRange(start, end)
            chunks.add(FileChunk(transferId, i, totalChunks, chunkData))
        }

        return chunks
    }

    /**
     * Overload accepting recipient public key as hex string.
     */
    fun sendFile(file: File, recipientPublicKeyHex: String): List<FileChunk> {
        return sendFile(file, hexToBytes(recipientPublicKeyHex))
    }

    /**
     * Reassembles chunks, decrypts payload using recipient private key via X25519 agreement,
     * and verifies SHA-256 hash integrity on receipt.
     */
    fun receiveFile(chunks: List<FileChunk>, recipientPrivateKey: ByteArray? = null): File {
        require(chunks.isNotEmpty()) { "Cannot receive file from empty chunks" }

        val sortedChunks = chunks.sortedBy { it.chunkIndex }
        val transferId = sortedChunks.first().transferId
        val totalChunks = sortedChunks.first().totalChunks

        require(sortedChunks.size == totalChunks) {
            "Incomplete chunk set: received ${sortedChunks.size} of $totalChunks chunks"
        }

        // Reassemble payload
        val totalDataLength = sortedChunks.sumOf { it.data.size }
        val packagedBytes = ByteArray(totalDataLength)
        var offset = 0
        for (chunk in sortedChunks) {
            System.arraycopy(chunk.data, 0, packagedBytes, offset, chunk.data.size)
            offset += chunk.data.size
        }

        require(packagedBytes.size >= 76) { "Corrupt data package: size < 76 bytes" }

        // Unpack Header
        val ephemeralPubKeyBytes = packagedBytes.copyOfRange(0, 32)
        val iv = packagedBytes.copyOfRange(32, 44)
        val expectedHash = packagedBytes.copyOfRange(44, 76)
        val encryptedPayload = packagedBytes.copyOfRange(76, packagedBytes.size)

        // Derive Key via X25519
        val privKeyBytes = recipientPrivateKey ?: DACNIdentityManager.getInstance(context).getPrivateKey()
        val recipientPrivKeyParams = X25519PrivateKeyParameters(ensure32Bytes(privKeyBytes), 0)
        val ephemeralPubKeyParams = X25519PublicKeyParameters(ephemeralPubKeyBytes, 0)

        val agreement = X25519Agreement()
        agreement.init(recipientPrivKeyParams)
        val sharedSecret = ByteArray(32)
        agreement.calculateAgreement(ephemeralPubKeyParams, sharedSecret, 0)

        val aesKeyBytes = MessageDigest.getInstance("SHA-256").digest(sharedSecret)
        val aesKey = SecretKeySpec(aesKeyBytes, "AES")

        // Decrypt
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, aesKey, GCMParameterSpec(128, iv))
        val decryptedBytes = cipher.doFinal(encryptedPayload)

        // Verify SHA-256 Hash Integrity
        val actualHash = MessageDigest.getInstance("SHA-256").digest(decryptedBytes)
        if (!actualHash.contentEquals(expectedHash)) {
            throw IllegalStateException("File verification failed: SHA-256 hash mismatch on receipt")
        }

        // Save file to storage
        val outputDir = File(context.cacheDir, "dacn_transfers").apply { mkdirs() }
        val outputFile = File(outputDir, "transfer_$transferId.bin")
        outputFile.writeBytes(decryptedBytes)

        return outputFile
    }

    /**
     * Helper to receive chunks directly without explicit private key parameter.
     */
    fun receiveFile(chunks: List<FileChunk>): File {
        return receiveFile(chunks, null)
    }

    private fun ensure32Bytes(bytes: ByteArray): ByteArray {
        return when {
            bytes.size == 32 -> bytes
            bytes.size > 32 -> bytes.copyOfRange(0, 32)
            else -> ByteArray(32).also { System.arraycopy(bytes, 0, it, 0, bytes.size) }
        }
    }

    private fun hexToBytes(hex: String): ByteArray {
        val result = ByteArray(hex.length / 2)
        for (i in result.indices) {
            val index = i * 2
            result[i] = hex.substring(index, index + 2).toInt(16).toByte()
        }
        return result
    }
}
