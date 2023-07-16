package dev.astler.unlib_compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import dev.astler.catlib.utils.infoLog
import dev.astler.unlib_compose.data.settings.AppTheme
import dev.astler.unlib_compose.data.settings.Settings

val Red200 = Color(0xfff297a2)
val Red300 = Color(0xffea6d7e)
val Red400 = Color(0xFFEF5350)
val Red700 = Color(0xffdd0d3c)
val Red800 = Color(0xffd00036)
val Red900 = Color(0xffc20029)
val Yellow = Color(0xFFFFC929)
val RedMy = Color(0xFF9E2020)
val RedMy2 = Color(0xFFC62828)

private val DarkColorPalette = darkColors(
    primary = Red300,
    primaryVariant = Red700,
    onPrimary = Color.Black,
    secondary = Red400,
    onSecondary = Color.White,
    error = Red200
)
private val LightColorPalette = lightColors(
    primary = RedMy2,
    primaryVariant = Red900,
    onPrimary = Color.White,
    secondary = Red700,
    secondaryVariant = Red900,
    onSecondary = Color.White,
    error = Red800
)

private val LightThemeColors = lightColorScheme()
private val DarkThemeColors = darkColorScheme()

fun supportsDynamic(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
fun CatComposeTheme(pSettings: Settings?, content: @Composable () -> Unit) {
    val isMaterial = pSettings?.materialTheme?.collectAsState()?.value == true || pSettings == null

    if (pSettings == null || isMaterial) {
        infoLog("material3 theme")

        val inDarkMode: Boolean = isSystemInDarkTheme()

        val colors = if (supportsDynamic() && isMaterial) {
            val context = LocalContext.current

            infoLog("material3 dynamic")

            if (inDarkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            infoLog("material3 static")

            if (inDarkMode) DarkThemeColors else LightThemeColors
        }

        MaterialTheme(
            colorScheme = colors,
            content = content
        )
    } else {
        infoLog("material theme")
        val theme = pSettings.themeStream.collectAsState()

        val nUseDarkColors = when (theme.value) {
            AppTheme.SYSTEM -> isSystemInDarkTheme()
            AppTheme.LIGHT -> false
            AppTheme.DARK -> true
        }

        val colors = if (nUseDarkColors) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        androidx.compose.material.MaterialTheme(
            colors = colors,
            typography = UnLibTypography,
            shapes = UnLibShapes,
            content = content
        )
    }
}
