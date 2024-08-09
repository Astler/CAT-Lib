package dev.astler.ui.activities.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.astler.ui.activities.CatActivity
import dev.astler.ui.fragments.IInternetDependentFragment
import dev.astler.ui.interfaces.INetworkActivity
import dev.astler.catlib.extensions.isOnline
import dev.astler.catlib.helpers.infoLog
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch

class NetworkActivityExtension(private val _catActivity: CatActivity, snapParentView: View) {

    private var _networkActivity: INetworkActivity? = null
    private var isNetworkCallbackRegistered = false

    private val _connectivityManager: ConnectivityManager by lazy {
        _catActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val _networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                infoLog("NetworkActivityExtension: onAvailable")
                _networkActivity?.onInternetConnected(network)
                if (_catActivity.activeFragment is IInternetDependentFragment) {
                    _catActivity.lifecycleScope.launch {
                        _catActivity.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                            (_catActivity.activeFragment as IInternetDependentFragment).onInternetAvailable()
                        }
                    }
                }
            }

            override fun onLost(network: Network) {
                infoLog("NetworkActivityExtension: onLost")
                _networkActivity?.onInternetLost(network)
                if (_catActivity.activeFragment is IInternetDependentFragment) {
                    _catActivity.lifecycleScope.launch {
                        _catActivity.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                            (_catActivity.activeFragment as IInternetDependentFragment).onInternetLost()
                        }
                    }
                }
            }
        }
    }


    init {
        infoLog("NetworkActivityExtension: init")

        if (_catActivity is INetworkActivity) {
            _networkActivity = _catActivity

            _catActivity.addOnFragmentChangedListener { fragment ->
                if (fragment is IInternetDependentFragment) {
                    if (_catActivity.isOnline) {
                        fragment.onInternetAvailable()
                    } else {
                        fragment.onInternetLost()
                    }
                }
            }

            infoLog("NetworkActivityExtension: loadNetworkCallback")
            loadNetworkCallback()
        } else {
            infoLog("NetworkActivityExtension: error, _catActivity is not INetworkActivity")
        }
    }

    private fun loadNetworkCallback() {
        _catActivity.lifecycleScope.launch {
            _catActivity.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (!isNetworkCallbackRegistered) {
                    infoLog("NetworkActivityExtension: STARTED")
                    _connectivityManager.registerNetworkCallback(
                        NetworkRequest.Builder().build(),
                        _networkCallback
                    )
                    isNetworkCallbackRegistered = true
                }
                awaitCancellation()
            }
        }

        _catActivity.lifecycleScope.launch {
            _catActivity.lifecycle.repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                if (isNetworkCallbackRegistered) {
                    infoLog("NetworkActivityExtension: DESTROYED")
                    _connectivityManager.unregisterNetworkCallback(_networkCallback)
                    isNetworkCallbackRegistered = false
                }
            }
        }
    }
}