package dev.astler.unlib_compose.ui.compose.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun EnchantedCheckbox(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    initialValue: Boolean = true,
    onValueChange: ((Boolean) -> Unit)? = null
) {
    val colors = MaterialTheme.colorScheme
    val (checkedState, onStateChange) = remember { mutableStateOf(initialValue) }
    Row(
        modifier
            .toggleable(
                value = checkedState,
                onValueChange = {
                    val newValue = !checkedState
                    onStateChange(newValue)
                    onValueChange?.invoke(newValue)
                },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = null
        )
        Text(
            color = colors.primary,
            text = stringResource(id = text),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
    }
}