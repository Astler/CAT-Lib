package dev.astler.unli.utils

import android.os.Build

class VersioningUtils {
    companion object {
        fun isQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}