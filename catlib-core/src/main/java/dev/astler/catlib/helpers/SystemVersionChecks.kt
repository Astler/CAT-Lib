package dev.astler.catlib.helpers

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
fun isQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

val isO get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

val isNotAtLeastO get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.O

fun isNotM() = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
fun isN() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
fun isP() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
fun isR() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
