package dev.astler.catlib.extensions

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dev.astler.catlib.helpers.isNotAtLeastO

fun Context.areNotificationsEnabled(): Boolean {
    return NotificationManagerCompat.from(this).areNotificationsEnabled()
}

fun Activity.requestNotificationsPermission() {
    if (areNotificationsEnabled()) return

    if (Build.VERSION.SDK_INT >= 33) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
        } else {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
            } else {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
            }
            startActivity(intent)
        }
    }
}


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