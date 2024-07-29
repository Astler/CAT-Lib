package dev.astler.ui.activities

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import dev.astler.ui.theme.CatComposeTheme

abstract class ComposeCatActivity : CatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(ComposeView(this).apply {
            setContent {
                CatComposeTheme {
                    ActivityContent()
                }
            }
        })
    }

    @Composable
    protected abstract fun ActivityContent()
}
