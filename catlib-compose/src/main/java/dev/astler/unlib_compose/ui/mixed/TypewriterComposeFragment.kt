package dev.astler.unlib_compose.ui.mixed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.astler.unlib_compose.ui.compose.text.AnimateTypewriterText
import dev.astler.unlib_compose.ui.compose.text.TypewriterText

class TypewriterComposeFragment : CatComposeFragment() {
    @Composable
    override fun ScreenContent() {
        Box(modifier = Modifier.statusBarsPadding()) {
            Box(modifier = Modifier.imePadding()) {
                val animatePartsList = remember {
                    listOf(
                        "reach your goals.",
                        "achieve your dreams.",
                        "be happy.",
                        "be healthy.",
                        "get rid of depression."
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    AnimateTypewriterText(
                        "Everything you need to ",
                        "Everything", animatePartsList
                    )
                    TypewriterText("Everything you need to reach your goals.")
                }
            }
        }
    }
}
