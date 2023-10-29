package dev.astler.catlib.extensions

import android.annotation.SuppressLint
import android.text.format.DateUtils
import dev.astler.catlib.helpers.trackedTry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

@SuppressLint("SimpleDateFormat")
fun Long.toTimeAgo(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    sdf.timeZone = TimeZone.getTimeZone("GMT")

    return trackedTry(fallbackValue = "Cant parse this time: $this") {
        val now = System.currentTimeMillis()
        DateUtils.getRelativeTimeSpanString(this, now, DateUtils.MINUTE_IN_MILLIS).toString()
    }
}

fun Date.toDMY(): String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)

fun Date.toDMYT(): String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(this)

fun Long.toDMY(): String = Date(this).toDMY()
fun Long.toDMYT(): String = Date(this).toDMYT()

fun String.toMilliseconds(): Long {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = simpleDateFormat.parse(this)
    return date?.time ?: 0L
}
