package com.bitchat.android.dacn.identity

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Singleton managing persistent Ed25519 identity keypairs and peer identification for DACN.
 */
object DACNIdentityManager {

    private const val TAG = "DACNIdentityManager"
    private const val PREFS_FILE = "dacn_identity_prefs"
    private const val KEY_PUBLIC_KEY = "key_ed25519_public"
    private const val KEY_PRIVATE_KEY = "key_ed25519_private"
    private const val KEY_DISPLAY_NAME = "key_display_name"
    private const val DEFAULT_DISPLAY_NAME = "DACN User"

    @Volatile
    private var prefs: SharedPreferences? = null

    @Volatile
    private var cachedPublicKey: ByteArray? = null

    @Volatile
    private var cachedPrivateKey: ByteArray? = null

    @Volatile
    private var cachedPeerID: String? = null

    @Volatile
    private var cachedDisplayName: String = DEFAULT_DISPLAY_NAME

    @Volatile
    private var isInitialized = false

    /**
     * Initializes the DACNIdentityManager singleton.
     * Loads existing keypair from EncryptedSharedPreferences or generates a new Ed25519 keypair on first launch.
     */
    @Synchronized
    fun initialize(context: Context) {
        if (isInitialized && prefs != null) return

        val appContext = context.applicationContext
        try {
            val sharedPreferences = initEncryptedSharedPreferences(appContext)
            prefs = sharedPreferences

            val pubHex = sharedPreferences.getString(KEY_PUBLIC_KEY, null)
            val privHex = sharedPreferences.getString(KEY_PRIVATE_KEY, null)

            if (!pubHex.isNullOrEmpty() && !privHex.isNullOrEmpty()) {
                Log.i(TAG, "Loading existing DACN Ed25519 identity from secure storage...")
                val pubBytes = hexToBytes(pubHex)
                val privBytes = hexToBytes(privHex)
                cachedPublicKey = pubBytes
                cachedPrivateKey = privBytes
                cachedPeerID = computePeerID(pubBytes)
            } else {
                Log.i(TAG, "No existing identity found. Generating new Ed25519 keypair on first launch...")
                generateAndSaveKeypair(sharedPreferences)
            }

            cachedDisplayName = sharedPreferences.getString(KEY_DISPLAY_NAME, DEFAULT_DISPLAY_NAME) ?: DEFAULT_DISPLAY_NAME
            isInitialized = true
            Log.i(TAG, "DACNIdentityManager initialized successfully with peerID: $cachedPeerID")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing DACNIdentityManager", e)
        }
    }

    private fun initEncryptedSharedPreferences(context: Context): SharedPreferences {
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
            Log.e(TAG, "EncryptedSharedPreferences failed, falling back to standard SharedPreferences", e)
            context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
        }
    }

    private fun generateAndSaveKeypair(sp: SharedPreferences) {
        val keyGen = Ed25519KeyPairGenerator()
        keyGen.init(Ed25519KeyGenerationParameters(SecureRandom()))
        val keyPair = keyGen.generateKeyPair()

        val privParams = keyPair.private as Ed25519PrivateKeyParameters
        val pubParams = keyPair.public as Ed25519PublicKeyParameters

        val privBytes = privParams.encoded
        val pubBytes = pubParams.encoded

        val pubHex = bytesToHex(pubBytes)
        val privHex = bytesToHex(privBytes)

        sp.edit()
            .putString(KEY_PUBLIC_KEY, pubHex)
            .putString(KEY_PRIVATE_KEY, privHex)
            .apply()

        cachedPublicKey = pubBytes
        cachedPrivateKey = privBytes
        cachedPeerID = computePeerID(pubBytes)
        Log.i(TAG, "Generated and stored new Ed25519 keypair securely.")
    }

    /**
     * Derives peerID as SHA-256 hash of public key bytes, returned as lowercase hex string.
     */
    private fun computePeerID(publicKeyBytes: ByteArray): String {
        val hash = MessageDigest.getInstance("SHA-256").digest(publicKeyBytes)
        return bytesToHex(hash).lowercase()
    }

    /**
     * Returns the 32-byte public key array.
     */
    fun getPublicKey(): ByteArray {
        checkInitialized()
        return cachedPublicKey?.clone() ?: throw IllegalStateException("Public key not available. Call initialize(context) first.")
    }

    /**
     * Returns the 32-byte private key seed array.
     */
    fun getPrivateKey(): ByteArray {
        checkInitialized()
        return cachedPrivateKey?.clone() ?: throw IllegalStateException("Private key not available. Call initialize(context) first.")
    }

    /**
     * Returns the peerID (SHA-256 hash of public key bytes as lowercase hex string).
     */
    fun getPeerID(): String {
        checkInitialized()
        return cachedPeerID ?: throw IllegalStateException("PeerID not available. Call initialize(context) first.")
    }

    /**
     * Returns the persisted user display name.
     */
    fun getDisplayName(): String {
        checkInitialized()
        return cachedDisplayName
    }

    /**
     * Allows user to set and persist a display name in EncryptedSharedPreferences.
     */
    @Synchronized
    fun setDisplayName(context: Context, name: String) {
        initialize(context)
        val sp = prefs ?: initEncryptedSharedPreferences(context.applicationContext)
        sp.edit().putString(KEY_DISPLAY_NAME, name).apply()
        cachedDisplayName = name
        Log.i(TAG, "Updated display name to: $name")
    }

    /**
     * Returns the 64-character lowercase hex encoded public key.
     */
    fun getPublicKeyHex(): String {
        checkInitialized()
        return bytesToHex(getPublicKey()).lowercase()
    }

    private fun checkInitialized() {
        if (!isInitialized || cachedPublicKey == null) {
            Log.w(TAG, "DACNIdentityManager was accessed before explicit initialization.")
        }
    }

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
