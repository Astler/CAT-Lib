package dev.astler.unlib_compose.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.astler.unlib_compose.R

private val GoogleSans = FontFamily(
    Font(R.font.google_sans_reg),
    Font(R.font.google_sans_bold, FontWeight.Bold)
)

val UnLibTypography = Typography(
    h4 = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.W600,
        fontSize = 26.sp
    ),
    h5 = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.W600,
        fontSize = 22.sp
    ),
    h6 = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    body1 = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = GoogleSans,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    )
)
