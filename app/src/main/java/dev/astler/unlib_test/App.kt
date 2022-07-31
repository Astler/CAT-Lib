package dev.astler.unlib_test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.StrictMode
import androidx.core.content.ContextCompat
import dev.astler.unlib_ads.AdsUnLibApp

class App : AdsUnLibApp() {

    companion object {
        const val MAIN_CHANNEL_ID = "MAIN_CHANNEL"
        const val NEWS_CHANNEL_ID = "NEWS_CHANNEL"
        const val MAIN_CHANNEL_NAME = "Main" // getString(R.string.notifications_channel_main)
        const val NEWS_CHANNEL_NAME = "News" // getString(R.string.notifications_channel_news)
    }

    override fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) ?: return

        listOf(
            NotificationChannel(
                MAIN_CHANNEL_ID,
                MAIN_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ),
            NotificationChannel(
                NEWS_CHANNEL_ID,
                NEWS_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        ).forEach { notificationManager.createNotificationChannel(it) }
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }
}
