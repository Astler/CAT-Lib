package dev.astler.unlib.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
fun isQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun isNotM() = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

/**
 * 24
 */

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
fun isN() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP)
fun isL() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
fun isP() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
fun isR() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
