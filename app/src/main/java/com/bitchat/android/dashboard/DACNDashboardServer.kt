package com.bitchat.android.dashboard

import android.content.Context
import android.util.Log
import com.bitchat.android.dacn.identity.DACNIdentityManager
import com.bitchat.android.service.MeshServiceHolder
import com.bitchat.android.services.AppStateStore
import com.bitchat.android.services.meshgraph.MeshGraphService
import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD

/**
 * Embedded HTTP server running NanoHTTPD on port 8080 exposing REST endpoints for DACN.
 */
class DACNDashboardServer private constructor(
    private val context: Context,
    port: Int = DEFAULT_PORT
) : NanoHTTPD(port) {

    private val gson = Gson()

    companion object {
        private const val TAG = "DACNDashboardServer"
        const val DEFAULT_PORT = 8080

        @Volatile
        private var instance: DACNDashboardServer? = null

        @Synchronized
        fun startServer(context: Context, port: Int = DEFAULT_PORT): DACNDashboardServer {
            val current = instance
            if (current != null && current.isAlive) {
                return current
            }
            val server = DACNDashboardServer(context.applicationContext, port)
            try {
                server.start(SOCKET_READ_TIMEOUT, false)
                Log.i(TAG, "DACN Dashboard API server started on port $port")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start DACN Dashboard API server on port $port", e)
            }
            instance = server
            return server
        }

        @Synchronized
        fun stopServer() {
            instance?.let { server ->
                try {
                    if (server.isAlive) {
                        server.stop()
                        Log.i(TAG, "DACN Dashboard API server stopped")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error stopping DACN Dashboard API server", e)
                }
            }
            instance = null
        }
    }

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        val method = session.method

        if (method == Method.GET) {
            when (uri) {
                "/api/identity" -> return handleIdentityRequest()
                "/api/mesh" -> return handleMeshRequest()
            }
        }

        return newFixedLengthResponse(
            Response.Status.NOT_FOUND,
            "application/json",
            gson.toJson(mapOf("error" to "Endpoint not found", "uri" to uri))
        )
    }

    private fun handleIdentityRequest(): Response {
        DACNIdentityManager.initialize(context)
        val identityData = mapOf(
            "publicKey" to DACNIdentityManager.getPublicKeyHex(),
            "peerID" to DACNIdentityManager.getPeerID(),
            "displayName" to DACNIdentityManager.getDisplayName()
        )
        return newFixedLengthResponse(
            Response.Status.OK,
            "application/json",
            gson.toJson(identityData)
        )
    }

    private fun handleMeshRequest(): Response {
        val bluetoothMesh = MeshServiceHolder.meshService
        val directPeers = try { AppStateStore.getDirectPeers() } catch (_: Exception) { emptySet<String>() }
        val activeCount = bluetoothMesh?.getActivePeerCount() ?: directPeers.size

        val graphSnapshot = try { MeshGraphService.getInstance().graphState.value } catch (_: Exception) { null }

        val peersList = directPeers.map { peerId ->
            mapOf(
                "peerId" to peerId,
                "hopCount" to 1,
                "isDirect" to true
            )
        }

        val meshData = mapOf(
            "connectedPeers" to directPeers.toList(),
            "connectedPeersCount" to activeCount,
            "peers" to peersList,
            "hopCounts" to mapOf(
                "direct" to directPeers.size,
                "graphNodes" to (graphSnapshot?.nodes?.size ?: 0)
            ),
            "messageDeliveryStats" to mapOf(
                "connected" to (activeCount > 0),
                "activeTransport" to "BLE",
                "directPeersCount" to directPeers.size,
                "totalSent" to 0,
                "totalReceived" to 0,
                "deliveredCount" to 0
            )
        )

        return newFixedLengthResponse(
            Response.Status.OK,
            "application/json",
            gson.toJson(meshData)
        )
    }
}
