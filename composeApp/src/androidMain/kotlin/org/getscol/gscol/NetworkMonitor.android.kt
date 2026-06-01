package org.getscol.gscol

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

/**
 * Android-only network monitor that updates the common `NetworkStatus` holder.
 */
object NetworkMonitor {
    private var callback: ConnectivityManager.NetworkCallback? = null

    fun start(context: Context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return

        // Set initial state
        val active = cm.activeNetwork
        val caps = cm.getNetworkCapabilities(active)
        val hasInternet = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        NetworkStatus.isAvailable.value = hasInternet

        // Already registered?
        if (callback != null) return

        callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val c = cm.getNetworkCapabilities(network)
                val ok = c?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true && c.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                NetworkStatus.isAvailable.value = ok
            }

            override fun onLost(network: Network) {
                NetworkStatus.isAvailable.value = false
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                val ok = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

                NetworkStatus.isAvailable.value = ok
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        callback?.let { cm.registerNetworkCallback(request, it) }
    }
}

