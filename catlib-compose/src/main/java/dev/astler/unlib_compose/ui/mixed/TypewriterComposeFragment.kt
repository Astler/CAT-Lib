package dev.astler.unlib_compose.ui.mixed

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.astler.unlib_compose.ui.compose.text.AnimateTypewriterText
import dev.astler.unlib_compose.ui.compose.text.TypewriterText

class TypewriterComposeFragment : CatComposeFragment() {
    @Composable
    override fun ScreenContent() {
        val animatePartsList = remember {
            listOf(
                "reach your goals.",
                "achieve your dreams.",
                "be happy.",
                "be healthy.",
                "get rid of depression."
            )
        }

        Column {
            AnimateTypewriterText(
                "Everything you need to ",
                "Everything", animatePartsList
            )
            TypewriterText("Everything you need to reach your goals.")
        }
    }
}
