package dev.astler.catlib.extensions

import android.content.Context
import dev.astler.catlib.helpers.isN
import java.util.Locale

val Context.activeLanguage: String
    get() = run {
        val configuration = resources.configuration
        val locale: Locale = if (isN()) {
            configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            configuration.locale
        }

        locale.language
    }