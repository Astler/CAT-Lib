package dev.astler.unlib_compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import dev.astler.unlib.utils.isAppDarkTheme

val Red200 = Color(0xfff297a2)
val Red300 = Color(0xffea6d7e)
val Red400 = Color(0xFFEF5350)
val Red700 = Color(0xffdd0d3c)
val Red800 = Color(0xffd00036)
val Red900 = Color(0xffc20029)
val Yellow = Color(0xFFFFC929)
val RedMy = Color(0xFF9E2020)
val RedMy2 = Color(0xFFC62828)

private val DarkColors = darkColors(
    primary = Red300,
    primaryVariant = Red700,
    onPrimary = Color.Black,
    secondary = Red400,
    onSecondary = Color.White,
    error = Red200
)
private val LightColors = lightColors(
    primary = RedMy2,
    primaryVariant = Red900,
    onPrimary = Color.White,
    secondary = Red700,
    secondaryVariant = Red900,
    onSecondary = Color.White,
    error = Red800
)

@Composable
fun isAppInDarkTheme(): Boolean {
    val pContext = LocalContext.current
    return pContext.isAppDarkTheme()
}

@Composable
fun UnLibTheme(darkTheme: Boolean = isAppInDarkTheme(), content: @Composable () -> Unit) {

    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = UnLibTypography,
        shapes = UnLibShapes,
        content = content
    )
}
