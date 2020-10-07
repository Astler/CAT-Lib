package dev.astler.unli.utils

import android.content.Context
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter

fun Context.writeToFileInFolder(pFileData: String, pFileName: String, pNameFolder: String = "", pFileType: String = ".json") {
    try {
        val nFile = if (pNameFolder.isNotEmpty()) {
            val nDir = File(filesDir.absolutePath + "/$pNameFolder")
            if(!nDir.exists()) nDir.mkdir()
            File(nDir.absolutePath + "/$pFileName$pFileType")
        }
        else {
            File(filesDir.absolutePath + "/$pFileName$pFileType")
        }


        val outputStreamWriter = OutputStreamWriter(nFile.outputStream())
        outputStreamWriter.write(pFileData)
        outputStreamWriter.close()
    } catch (e: IOException) {
        log("UNLI: File write failed: $e")
    }
}