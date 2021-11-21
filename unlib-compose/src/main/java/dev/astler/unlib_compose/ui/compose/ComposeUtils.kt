package dev.astler.unlib_compose.ui.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib_compose.theme.getPrefsTextStyle

@Composable
fun PrefsText(text: String, modifier: Modifier = Modifier, bold: Boolean = false) {
    Text(
        modifier = modifier,
        text = text,
        style = getPrefsTextStyle(bold),
        color = MaterialTheme.colors.onBackground,
        fontSize = gPreferencesTool.mTextSize.sp
    )
}
