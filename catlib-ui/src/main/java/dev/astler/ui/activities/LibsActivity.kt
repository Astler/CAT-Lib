package dev.astler.ui.activities

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.compose.screens.LibsScreen

@AndroidEntryPoint
class LibsActivity : ComposeCatActivity() {

    @Composable
    override fun ActivityContent() {
        LibsScreen()
    }
}
