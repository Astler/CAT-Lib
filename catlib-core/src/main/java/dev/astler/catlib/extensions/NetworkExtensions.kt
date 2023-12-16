package dev.astler.catlib.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dev.astler.catlib.constants.IODispatcher
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.helpers.isNotM
import dev.astler.catlib.helpers.trackedTry
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Suppress("DEPRECATION")
val Context.isOnline: Boolean
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (isNotM()) {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo?.type == ConnectivityManager.TYPE_MOBILE
        } else {
            val network = connectivityManager.activeNetwork

            if (network != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    )
                }
            }
        }

        return false
    }


suspend fun String.getJsonContent(fallbackAction: (() -> String)? = null): String {
    return withContext(IODispatcher) {
        var connection: HttpURLConnection? = null

        trackedTry(finallyAction = {
            connection?.disconnect()
        }, fallbackValue = "") {
            val url = URL(this@getJsonContent)
            connection = url.openConnection() as HttpURLConnection

            var result = ""

            connection?.let {
                it.connectTimeout = 15000
                it.readTimeout = 15000
                it.requestMethod = "GET"

                if (it.responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader(InputStreamReader(it.inputStream)).use { reader ->
                        val response = StringBuilder()
                        var line: String?

                        while (reader.readLine().also { line = it } != null) {
                            response.append(line).append("\n")
                        }

                        result = response.toString()
                    }
                } else {
                    errorLog("Error: Server responded with status code: ${connection?.responseCode}")
                    result = fallbackAction?.invoke() ?: ""
                }
            }

            result
        }
    }
}
