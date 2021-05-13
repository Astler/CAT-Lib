package dev.astler.unlib.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun Date.toHumanViewDMY(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)
}

fun Date.toHumanViewDMYT(): String {
    return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(this)
}

fun String.humanViewToMillis(): Long {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val date = simpleDateFormat.parse(this)

    return date?.time ?: 0L
}

fun Int.timeToString(): String {
    val seconds = this % 60
    val minutes = (this - seconds) / 60 % 60
    val hours = (this - minutes - seconds) / 3600
    val h = if (hours < 10) "0$hours" else hours.toString()
    val m = if (minutes < 10) "0$minutes" else minutes.toString()
    val s = if (seconds < 10) "0$seconds" else seconds.toString()

    return "$h:$m:$s"
}

fun Long.millisToHumanViewDMY(): String {
    return Date(this).toHumanViewDMY()
}

fun Long.millisToHumanViewDMYT(): String {
    return Date(this).toHumanViewDMYT()
}