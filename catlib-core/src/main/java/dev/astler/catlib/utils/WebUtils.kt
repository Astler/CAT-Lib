package dev.astler.catlib.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

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
