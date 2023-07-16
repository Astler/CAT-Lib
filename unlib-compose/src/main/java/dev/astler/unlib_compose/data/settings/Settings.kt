package dev.astler.unlib_compose.data.settings

import kotlinx.coroutines.flow.StateFlow

enum class AppTheme {
    DARK,
    LIGHT,
    SYSTEM;

    companion object {
        fun fromString(string: String) = valueOf(string.uppercase())
    }
}

enum class AppLanguage {
    SYSTEM,
    EN,
    RU,
    UA;

    companion object {
        fun fromString(string: String) = valueOf(string.uppercase())
    }
}

interface Settings {
    val themeStream: StateFlow<AppTheme>
    val materialTheme: StateFlow<Boolean>
    val languageStream: StateFlow<AppLanguage>
    var theme: AppTheme
    var language: AppLanguage
}
