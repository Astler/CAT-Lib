package dev.astler.unlib_compose.data.settings

import kotlinx.coroutines.flow.StateFlow

enum class AppTheme {
    DARK,
    LIGHT,
    SYSTEM;

    companion object {
        fun fromString(string: String) = valueOf(string)
    }
}

interface Settings {
    val themeStream: StateFlow<AppTheme>
    var theme: AppTheme
}
