package dev.astler.catlib.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.* 

@SuppressLint("SimpleDateFormat")
fun Long.toTimeAgo(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val now = System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(this, now, DateUtils.MINUTE_IN_MILLIS).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
        errorLog(e)
    }

    return "Cant parse this time: $this"
}

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

fun Long.millisToHumanViewDMY(): String {
    return Date(this).toHumanViewDMY()
}

fun Long.millisToHumanViewDMYT(): String {
    return Date(this).toHumanViewDMYT()
}

/**
 * From KAHelpers
 */

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
