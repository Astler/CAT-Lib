package dev.astler.catlib.extensions

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dev.astler.catlib.helpers.trackedTry
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.channels.FileChannel
import java.nio.charset.Charset

fun Context.getBitmapFromUri(uri: Uri): Bitmap? {
    return trackedTry(fallbackValue = null) {
        val imageStream = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(imageStream)
    }
}

fun Bitmap.saveToInternalStorage(content: Context, pictureName: String = "shareImage.png"): Uri {
    val wrapper = ContextWrapper(content)
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)
    file = File(file, pictureName)

    return saveToInternalStorage(content, file)
}

fun Bitmap.saveToInternalStorage(content: Context, file: File): Uri {
    trackedTry {
        val stream: OutputStream = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
    }

    return Uri.parse(file.absolutePath)
}

fun File.readInternalFile(): String {
    return trackedTry(fallbackValue = "I can\'t read this file! $absolutePath") {
        val inputStream = inputStream()
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()

        String(byteArray, Charset.defaultCharset())
    }
}

fun Context.writeInternalFile(content: String, name: String, path: String = "", fileType: String = ".json") {
    trackedTry {
        val fileName = name + fileType

        val file = if (path.isNotEmpty()) {
            val internalFilesDirectory = File(filesDir.absolutePath + "/$path")
            if (!internalFilesDirectory.exists()) internalFilesDirectory.mkdir()
            File(internalFilesDirectory.absolutePath + "/$fileName")
        } else {
            File(filesDir.absolutePath + "/$fileName")
        }

        val outputStreamWriter = OutputStreamWriter(file.outputStream())
        outputStreamWriter.write(content)
        outputStreamWriter.close()
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
