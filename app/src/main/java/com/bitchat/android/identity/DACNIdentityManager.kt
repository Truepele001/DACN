package com.bitchat.android.identity

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import java.security.SecureRandom

/**
 * Manages persistent Ed25519 keypairs and identity information for DACN peer identification.
 */
class DACNIdentityManager private constructor(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        initEncryptedSharedPreferences()
    }

    private var cachedPublicKey: ByteArray? = null
    private var cachedPrivateKey: ByteArray? = null
    private var cachedPeerID: String? = null

    init {
        ensureIdentityExists()
    }

    companion object {
        private const val TAG = "DACNIdentityManager"
        private const val PREFS_FILE = "dacn_identity_secure_prefs"
        private const val KEY_PUBLIC_KEY = "key_ed25519_public"
        private const val KEY_PRIVATE_KEY = "key_ed25519_private"
        private const val KEY_DISPLAY_NAME = "key_display_name"
        private const val DEFAULT_DISPLAY_NAME = "DACN User"

        @Volatile
        private var instance: DACNIdentityManager? = null

        fun getInstance(context: Context): DACNIdentityManager {
            return instance ?: synchronized(this) {
                instance ?: DACNIdentityManager(context.applicationContext).also { instance = it }
            }
        }
    }

    private fun initEncryptedSharedPreferences(): SharedPreferences {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREFS_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            Log.e(TAG, "EncryptedSharedPreferences initialization failed, falling back to standard prefs", e)
            context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
        }
    }

    @Synchronized
    private fun ensureIdentityExists() {
        val pubHex = prefs.getString(KEY_PUBLIC_KEY, null)
        val privHex = prefs.getString(KEY_PRIVATE_KEY, null)

        if (pubHex.isNullOrEmpty() || privHex.isNullOrEmpty()) {
            generateAndSaveNewIdentity()
        } else {
            cachedPublicKey = hexToBytes(pubHex)
            cachedPrivateKey = hexToBytes(privHex)
            cachedPeerID = pubHex
        }
    }

    private fun generateAndSaveNewIdentity() {
        Log.i(TAG, "Generating new Ed25519 identity keypair...")
        val keyGen = Ed25519KeyPairGenerator()
        keyGen.init(Ed25519KeyGenerationParameters(SecureRandom()))
        val keyPair = keyGen.generateKeyPair()

        val privParams = keyPair.private as Ed25519PrivateKeyParameters
        val pubParams = keyPair.public as Ed25519PublicKeyParameters

        val privBytes = privParams.encoded
        val pubBytes = pubParams.encoded

        val pubHex = bytesToHex(pubBytes)
        val privHex = bytesToHex(privBytes)

        prefs.edit()
            .putString(KEY_PUBLIC_KEY, pubHex)
            .putString(KEY_PRIVATE_KEY, privHex)
            .apply()

        cachedPublicKey = pubBytes
        cachedPrivateKey = privBytes
        cachedPeerID = pubHex
    }

    /**
     * Returns the 32-byte Ed25519 public key.
     */
    fun getPublicKey(): ByteArray {
        ensureIdentityExists()
        return cachedPublicKey!!.clone()
    }

    /**
     * Returns the 32-byte Ed25519 private key seed.
     */
    fun getPrivateKey(): ByteArray {
        ensureIdentityExists()
        return cachedPrivateKey!!.clone()
    }

    /**
     * Returns the hex-encoded persistent public key serving as the peerID.
     */
    fun getPeerID(): String {
        ensureIdentityExists()
        return cachedPeerID!!
    }

    /**
     * Returns the persistent user display name.
     */
    fun getDisplayName(): String {
        return prefs.getString(KEY_DISPLAY_NAME, DEFAULT_DISPLAY_NAME) ?: DEFAULT_DISPLAY_NAME
    }

    /**
     * Sets a persistent user display name that survives app restarts.
     */
    fun setDisplayName(name: String) {
        prefs.edit().putString(KEY_DISPLAY_NAME, name).apply()
    }

    fun getPublicKeyHex(): String = getPeerID()

    fun getPrivateKeyHex(): String = bytesToHex(getPrivateKey())

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
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
