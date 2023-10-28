package dev.astler.unlib_test.fragments.cat

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import dev.astler.catlib.extensions.getValueOrDefault
import dev.astler.catlib.extensions.toast
import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.helpers.trackedTry
import dev.astler.unlib_compose.ui.mixed.CatComposeFragment
import dev.astler.unlib_test.R

@AndroidEntryPoint
class CatCodeFragment : CatComposeFragment() {

    @Composable
    override fun ScreenContent() {
        ButtonsGrid()
    }

    @Composable
    fun ButtonsGrid() {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Button(R.string.tracked_try_base_good) {
                trackedTry {
                    infoLog("Try Done!")
                    safeContext.toast("Try Done!")
                }
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.tracked_try_base_bad) {
                trackedTry {
                    throw Exception("Test Exception")
                    infoLog("Try Done!")
                }
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.tracked_try_finally_good) {
                trackedTry(finallyAction = {
                    infoLog("Finally Done!")
                    safeContext.toast("Finally Done!")
                }) {
                    infoLog("Try Done!")
                    safeContext.toast("Try Done!")
                }

            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.tracked_try_finally_bad) {
                trackedTry(finallyAction = {
                    infoLog("Finally Done!")
                    safeContext.toast("Finally Done!")
                }) {
                    throw Exception("Test Exception")
                    infoLog("Try Done!")
                    safeContext.toast("Try Done!")
                }
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.tracked_full_good) {
                trackedTry(finallyAction = {
                    infoLog("Finally Done!")
                    safeContext.toast("Finally Done!")
                }, errorCatchAction = {
                    infoLog("Error Done!")
                    safeContext.toast("Error Done!")
                }) {
                    infoLog("Try Done!")
                    safeContext.toast("Try Done!")
                }
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.tracked_full_bad) {
                trackedTry(finallyAction = {
                    infoLog("Finally Done!")
                    safeContext.toast("Finally Done!")
                }, errorCatchAction = {
                    infoLog("Error Done!")
                    safeContext.toast("Error Done!")
                }) {
                    throw Exception("Test Exception")
                    infoLog("Try Done!")
                    safeContext.toast("Try Done!")
                }
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.tracked_full_fallback_good) {
                val result = trackedTry(fallbackValue = 10, finallyAction = {
                    infoLog("Finally Done!")
                    safeContext.toast("Finally Done!")
                    3
                }, errorCatchAction = {
                    infoLog("Error Done!")
                    safeContext.toast("Error Done!")
                    2
                }) {
                    infoLog("Try Done!")
                    safeContext.toast("Try Done!")
                    1
                }

                infoLog("Result: $result")
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.tracked_full_fallback_bad) {
                val result = trackedTry(fallbackValue = 10, finallyAction = {
                    infoLog("Finally Done!")
                    safeContext.toast("Finally Done!")
                    3
                }, errorCatchAction = {
                    infoLog("Error Done!")
                    safeContext.toast("Error Done!")
                    2
                }) {
                    throw Exception("Test Exception")
                    infoLog("Try Done!")
                    safeContext.toast("Try Done!")
                    1
                }

                infoLog("Result: $result")
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.map_exist_value) {
                val map = mapOf("1" to "2", "2" to "3")
                val result = map.getValueOrDefault("1", "4")
                infoLog("Result: $result")
                safeContext.toast("Result: $result")
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.map_missed_value) {
                val map = mapOf("10" to "20", "20" to "30")
                val result = map.getValueOrDefault("1", "40")
                infoLog("Result: $result")
                safeContext.toast("Result: $result")
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(R.string.map_missed_value) {
                val map = mapOf("10" to "20", "20" to "30")
                val result = map.getValueOrDefault("1", "40")
                infoLog("Result: $result")
                safeContext.toast("Result: $result")
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