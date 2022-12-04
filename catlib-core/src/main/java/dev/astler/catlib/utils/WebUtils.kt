package dev.astler.catlib.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun String.getJsonContent(): String {
    var nConnection: HttpsURLConnection? = null

    try {
        val nURL = URL(this)
        nConnection = nURL.openConnection() as HttpsURLConnection
        nConnection.connect()

        val nBufferReader =
            BufferedReader(InputStreamReader(nConnection.inputStream))
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

        return nStringBuilder.toString()
    } catch (ex: MalformedURLException) {
        ex.printStackTrace()
        errorLog(ex)
    } catch (ex: IOException) {
        ex.printStackTrace()
        errorLog(ex)
    } finally {
        if (nConnection != null) {
            trySimple {
                nConnection.disconnect()
            }
        }
    }

    return ""
}