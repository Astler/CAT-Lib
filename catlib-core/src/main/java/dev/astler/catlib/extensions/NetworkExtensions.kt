package dev.astler.catlib.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dev.astler.catlib.helpers.isNotM
import dev.astler.catlib.helpers.trackedTry
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

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


fun String.getJsonContent(): String {
    var nConnection: HttpsURLConnection? = null

    return trackedTry(fallbackValue = "", finallyAction = {
        if (nConnection != null) {
            trackedTry {
                nConnection?.disconnect()
            }
        }

        ""
    }) {
        val nURL = URL(this)
        nConnection = nURL.openConnection() as HttpsURLConnection

        if (nConnection == null) return@trackedTry ""

        nConnection!!.connect()

        val nBufferReader =
            BufferedReader(InputStreamReader(nConnection!!.inputStream))
        val nStringBuilder = StringBuilder()
        var line: String
        while (nBufferReader.readLine().also { line = it ?: "" } != null) {
            nStringBuilder.append(
                """
                        $line
                        
                """.trimIndent()
            )
        }

        nBufferReader.close()

        nStringBuilder.toString()
    }
}
