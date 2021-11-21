package dev.astler.unlib.utils

import android.content.Context
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import dev.astler.unlib.gPreferencesTool

fun Context.isPaidVersion(): Boolean {
    return applicationContext.packageName.endsWith("paid")
}

fun Context.getSupportedLanguageCode(): String {
    return if (gPreferencesTool.isSystemLanguage) {
        val nSystemLanguage = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).toString()

        if (nSystemLanguage.contains("ru")) "ru" else "en"
    } else gPreferencesTool.appLanguage
}
