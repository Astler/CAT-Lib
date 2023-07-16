package dev.astler.unlib_test.fragments.firebase

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.utils.dialogs.okDialog
import dev.astler.catlib.remote_config.RemoteConfigProvider
import dev.astler.unlib_compose.ui.mixed.CatComposeFragment
import dev.astler.unlib_test.R
import javax.inject.Inject

@AndroidEntryPoint
class RemoteConfigFragment : CatComposeFragment() {

    @Inject
    lateinit var remoteConfigProvider: RemoteConfigProvider

    @Composable
    override fun ScreenContent() {
        ButtonsGrid()
    }

    @Composable
    fun ButtonsGrid() {
        Column {
            Button(R.string.get_test_string) {
                safeContext.okDialog(message = remoteConfigProvider.getString("test_string"))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Button(@StringRes title: Int, clicked: () -> Unit) {
        val colors = MaterialTheme.colorScheme

        OutlinedCard(
            modifier = Modifier.padding(8.dp),
            border = BorderStroke(1.dp, colors.onSurface),
            onClick = { clicked() },
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
                        text = stringResource(title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                        maxLines = 2,
                        minLines = 2
                    )
                }
            }
        }
    }
}