package dev.astler.unlib_compose.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewbinding.ViewBinding
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.unlib_compose.theme.CatComposeTheme

abstract class ComposeCatActivity : CatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(ComposeView(this).apply {
            setContent {
                CatComposeTheme() {
                    ActivityContent()
                }
            }
        })
    }

    @Composable
    protected abstract fun ActivityContent()
}
