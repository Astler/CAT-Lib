package dev.astler.unlib_test.fragments.tech

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.utils.isAppDarkTheme
import dev.astler.cat_ui.utils.isSystemDarkMode
import dev.astler.unlib_compose.ui.mixed.CatComposeFragment

@AndroidEntryPoint
class ThemePreviewFragment : CatComposeFragment() {

    private val menuItems = listOf(
        "system",
        "dark",
        "light",
        "auto",
    )

    @Composable
    override fun ScreenContent() {
        VariableGrid(menuItems)
    }

    @Composable
    fun VariableGrid(items: List<String>) {
        val colors = MaterialTheme.colorScheme

        val selectedItem = remember { mutableStateOf(preferences.appTheme) }

        Column {
            Text(
                text = stringResource(id = dev.astler.catlib.ui.R.string.app_theme),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = """
                    System theme: ${if (activity?.isSystemDarkMode == true) "dark" else "light"}
                    App Theme: ${if (safeContext.isAppDarkTheme(selectedItem.value)) "dark" else "light"}
                    Preferences Parameter: ${selectedItem.value}
                """.trimIndent(),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = colors.secondary,
            )

            Spacer(modifier = Modifier.padding(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(items) { item ->
                    GridItem(item, selectedItem.value) {
                        selectedItem.value = item
                        preferences.appTheme = item
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GridItem(item: String, selectedValue: String, onItemSelected: (String) -> Unit) {
        val colors = MaterialTheme.colorScheme
        val isSelected = item == selectedValue

        OutlinedCard(
            modifier = Modifier.padding(8.dp),
            border = BorderStroke(if (isSelected) 4.dp else 1.dp, if (isSelected) colors.primary else colors.onSurface),
            onClick = {
                onItemSelected.invoke(item)
            },
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .background(colors.surface, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) colors.primary else colors.onSurface,
                    )
                }
            }
        }
    }
}