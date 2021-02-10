package dev.astler.unlib.utils

import android.content.Context
import java.io.*
import java.nio.channels.FileChannel
import java.nio.charset.Charset

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
        infoLog("UNLI: File write failed: $e")
    }
}


fun File.copyDirectoryImpl(destDir: File) {
    val items = listFiles()
    if (items != null && items.isNotEmpty()) {
        for (anItem: File in items) {
            if (anItem.isDirectory) {
                // create the directory in the destination
                val newDir = File(destDir, anItem.name)

                println(
                        "CREATED DIR: "
                                + newDir.absolutePath
                )

                newDir.mkdir()

                // copy the directory (recursive call)
                anItem.copyDirectory(newDir)
            } else {
                // copy the file
                val destFile = File(destDir, anItem.name)
                anItem.copySingleFile(anItem, destFile)
            }
        }
    }
}

fun File.copyDirectory(destDir: File) {
    if (!destDir.exists()) {
        destDir.mkdirs()
    }

    require(exists()) { "sourceDir does not exist" }

    require(!(isFile || destDir.isFile)) { "Either sourceDir or destDir is not a directory" }

    copyDirectoryImpl(destDir)
}

fun File.copySingleFile(sourceFile: File, destFile: File) {
    println(
            "COPY FILE: " + sourceFile.absolutePath
                    + " TO: " + destFile.absolutePath
    )
    if (!destFile.exists()) {
        destFile.createNewFile()
    }
    var sourceChannel: FileChannel? = null
    var destChannel: FileChannel? = null
    try {
        sourceChannel = FileInputStream(sourceFile).channel
        destChannel = FileOutputStream(destFile).channel
        sourceChannel.transferTo(0, sourceChannel.size(), destChannel)
    } finally {
        sourceChannel?.close()
        destChannel?.close()
    }
}


fun Context.readFileFromAssets(path: String): String {
    return try {
        val inputStream = assets.open(path)
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()

        String(byteArray, Charset.defaultCharset())

    } catch (e: Exception) {
        "SWW"
    }
}

fun Context.readFileFromFiles(pFileWithPath: String): String {
    return try {
        val inputStream = File(filesDir, pFileWithPath).inputStream()
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()

        String(byteArray, Charset.defaultCharset())

    } catch (e: Exception) {
        "SWW"
    }
}