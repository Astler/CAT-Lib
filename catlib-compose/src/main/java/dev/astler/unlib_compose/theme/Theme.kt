package dev.astler.unlib_compose.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.astler.catlib.helpers.isS

val Red200 = Color(0xfff297a2)
val Red300 = Color(0xffea6d7e)
val Red400 = Color(0xFFEF5350)
val Red700 = Color(0xffdd0d3c)
val Red800 = Color(0xffd00036)
val Red900 = Color(0xffc20029)
val Yellow = Color(0xFFFFC929)
val RedMy = Color(0xFF9E2020)
val RedMy2 = Color(0xFFC62828)

private val LightThemeColors = lightColorScheme(
    primary = RedMy2,
    onPrimary = Color.White,
    secondary = Red700,
    onSecondary = Color.White,
    error = Red800
)

private val DarkThemeColors = darkColorScheme(
    primary = Red300,
    onPrimary = Color.Black,
    secondary = Red400,
    onSecondary = Color.White,
    error = Red200
)

@Composable
fun CatComposeTheme(isDarkTheme: Boolean = isSystemInDarkTheme(), isDynamicColors: Boolean = true, content: @Composable () -> Unit) {
    val colorScheme = when {
        isDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDarkTheme -> DarkThemeColors
        else -> LightThemeColors
    }
    val view = LocalView.current

//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.background.toArgb()
//            window.navigationBarColor = colorScheme.background.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
//            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
//                !isDarkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}