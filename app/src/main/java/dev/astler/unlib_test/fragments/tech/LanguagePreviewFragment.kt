package dev.astler.unlib_test.fragments.tech

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import dev.astler.ui.activities.BindingCatActivity
import dev.astler.catlib.extensions.activeLanguage
import dev.astler.ui.fragments.CatComposeFragment
import com.ao.subscribeme.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LanguagePreviewFragment : CatComposeFragment() {

    private val menuItems = listOf(
        Locale.ENGLISH,
        Locale("ru"),
        Locale("uk"),
    )

    @Composable
    override fun ScreenContent() {
        VariableGrid(menuItems)
    }

    @Composable
    fun VariableGrid(items: List<Locale>) {
        val colors = MaterialTheme.colorScheme

        Column {
            Text(
                text = stringResource(id = gg.pressf.resources.R.string.language),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface,
            )

            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = colors.secondary,
            )

            Text(
                text = activity?.applicationContext?.getString(R.string.app_name) ?: "TUPO NULL",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = colors.secondary,
            )

            Text(
                text = stringResource(id = androidx.core.R.string.call_notification_answer_action),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(items) { item ->
                    GridItem(item)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GridItem(item: Locale) {
        val colors = MaterialTheme.colorScheme

        val isSelected = safeContext.activeLanguage == item.language

        OutlinedCard(
            modifier = Modifier.padding(8.dp),
            border = BorderStroke(
                if (isSelected) 4.dp else 1.dp,
                if (isSelected) colors.primary else colors.onSurface
            ),
            onClick = {
                preferences.appLanguage = item.language
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
                        text = item.language,
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