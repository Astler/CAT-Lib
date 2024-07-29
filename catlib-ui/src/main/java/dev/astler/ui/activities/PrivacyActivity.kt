package dev.astler.ui.activities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.catlib.config.AppConfig
import dev.astler.ui.compose.screens.LibsScreen
import dev.astler.ui.compose.screens.PrivacyScreen
import javax.inject.Inject

@AndroidEntryPoint
class PrivacyActivity : ComposeCatActivity() {

    @Composable
    override fun ActivityContent() {
        Box(modifier = Modifier.statusBarsPadding()) {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                PrivacyScreen(appConfig)
            }
        }
    }
}
