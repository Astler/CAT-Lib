package dev.astler.catlib.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dev.astler.catlib.helpers.isNotAtLeastO

fun Context.createNotificationChannel(
    channelName: String = packageName,
    channelDescription: String = "",
    id: String = "unli_default"
) {
    if (isNotAtLeastO) return

    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(id, channelName, importance)
    channel.description = channelDescription

    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
}