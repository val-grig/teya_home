package com.grigoryev.teya_home.core.app.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log.e
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed class ConnectionEvent {
    object ConnectionLost : ConnectionEvent()
    object ConnectionRecovered : ConnectionEvent()
}

interface ConnectionRepository {
    fun getConnectionEvents(): Flow<ConnectionEvent>
}

@Singleton
class ConnectionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectionRepository {

    override fun getConnectionEvents(): Flow<ConnectionEvent> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            private var wasConnected = isConnected()

            override fun onAvailable(network: Network) {
                if (!wasConnected) {
                    trySend(ConnectionEvent.ConnectionRecovered)
                    wasConnected = true
                }
            }

            override fun onLost(network: Network) {
                if (wasConnected) {
                    trySend(ConnectionEvent.ConnectionLost)
                    wasConnected = false
                }
            }
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
    }

    private fun isConnected(): Boolean {
        return runCatching {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }.getOrDefault(true)
    }
}