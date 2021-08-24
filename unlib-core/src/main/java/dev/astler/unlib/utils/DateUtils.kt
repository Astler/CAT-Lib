package dev.astler.unlib.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.* // ktlint-disable no-wildcard-imports

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

/**
 * From KAHelpers
 */

fun Date.millisecondsSince(date: Date) = (time - date.time)
fun Date.secondsSince(date: Date): Double = millisecondsSince(date) / 1000.0
fun Date.minutesSince(date: Date): Double = secondsSince(date) / 60
fun Date.hoursSince(date: Date): Double = minutesSince(date) / 60
fun Date.daysSince(date: Date): Double = hoursSince(date) / 24
fun Date.weeksSince(date: Date): Double = daysSince(date) / 7
fun Date.monthsSince(date: Date): Double = weeksSince(date) / 4
fun Date.yearsSince(date: Date): Double = monthsSince(date) / 12

val currentDate get() = Date(System.currentTimeMillis())

inline val now: Long
    get() = Calendar.getInstance().timeInMillis

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isTomorrow(): Boolean {
    return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}
