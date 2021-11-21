package dev.astler.unlib.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.* // ktlint-disable no-wildcard-imports
import java.nio.channels.FileChannel
import java.nio.charset.Charset

fun Context.writeToFileInFolder(pFileData: String, pFileName: String, pNameFolder: String = "", pFileType: String = ".json") {
    try {
        val nFile = if (pNameFolder.isNotEmpty()) {
            val nDir = File(filesDir.absolutePath + "/$pNameFolder")
            if (!nDir.exists()) nDir.mkdir()
            File(nDir.absolutePath + "/$pFileName$pFileType")
        } else {
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
                    "CREATED DIR: " +
                        newDir.absolutePath
                )

                newDir.mkdir()

                anItem.copyDirectory(newDir)
            } else {
                val destFile = File(destDir, anItem.name)
                anItem.copySingleFile(destFile)
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

fun File.copySingleFile(destFile: File) {
    println(
        "COPY FILE: " + absolutePath +
            " TO: " + destFile.absolutePath
    )
    if (!destFile.exists()) {
        destFile.createNewFile()
    }
    var sourceChannel: FileChannel? = null
    var destChannel: FileChannel? = null
    try {
        sourceChannel = FileInputStream(this).channel
        destChannel = FileOutputStream(destFile).channel
        sourceChannel.transferTo(0, sourceChannel.size(), destChannel)
    } finally {
        sourceChannel?.close()
        destChannel?.close()
    }
}

fun Context.readFileFromRaw(pFileId: Int, pErrorReturn: String = ""): String {
    return try {
        val inputStream = resources.openRawResource(pFileId)
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()

        String(byteArray, Charset.defaultCharset())
    } catch (e: Exception) {
        infoLog("UNLIB: Error while reading raw file $e")
        pErrorReturn
    }
}

fun File.readFileFromFiles(): String {
    return try {
        val inputStream = inputStream()
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()

        String(byteArray, Charset.defaultCharset())
    } catch (e: Exception) {
        "I can\'t read this file! $absolutePath"
    }
}

fun Context.getBitmapFromAsset(strName: String): Bitmap? {
    return tryWithDefault(null) {
        BitmapFactory.decodeStream(assets.open(strName))
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

fun Bitmap.saveToInternalStorage(pContext: Context, pName: String = "shareImage.png"): Uri {
    val wrapper = ContextWrapper(pContext)

    var file = wrapper.getDir("images", Context.MODE_PRIVATE)

    file = File(file, pName)

    trySimple {
        val stream: OutputStream = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
    }

    return Uri.parse(file.absolutePath)
}

fun Bitmap.createLocalImage(context: Context, name: String) {
    trySimple {
        val file = File(context.filesDir, name)
        val out = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.JPEG, 80, out)
        out.close()
    }
}
