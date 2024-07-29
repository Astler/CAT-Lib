package dev.astler.ui.interfaces

import android.net.Network

interface INetworkActivity {
    fun onInternetConnected(network: Network) {}
    fun onInternetLost(network: Network) {}
}