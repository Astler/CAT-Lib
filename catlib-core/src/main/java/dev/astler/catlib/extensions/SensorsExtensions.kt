package dev.astler.catlib.extensions

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.helpers.isO


fun Context.tryToVibrate(preferences: PreferencesTool, duration: Long = 40L) {
    if (!preferences.vibrateOnClick) return

    vibrate(duration)
}

@Suppress("DEPRECATION")
fun Context.vibrate(duration: Long = 40L) {
    val vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    if (isO) {
        vibe.vibrate(VibrationEffect.createOneShot(duration, 10))
    } else {
        vibe.vibrate(duration)
    }
}