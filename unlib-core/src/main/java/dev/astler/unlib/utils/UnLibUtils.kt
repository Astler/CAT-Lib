package dev.astler.unlib.utils

import android.content.Context
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.paidPackagePostfix

fun Context.isPaidVersion(): Boolean {
    return applicationContext.packageName.endsWith(paidPackagePostfix)
}

fun Context.getSupportedLanguageCode(): String {
    return if (gPreferencesTool.isSystemLanguage) {
        val nSystemLanguage = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).toString()

        if (nSystemLanguage.contains("ru")) "ru" else "en"
    } else gPreferencesTool.appLanguage
}
