package dev.astler.unlib.utils

import android.os.Build

fun isQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun isNotM() = Build.VERSION.SDK_INT < Build.VERSION_CODES.M