package dev.astler.unlib_compose.ui.mixed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import dev.astler.cat_ui.fragments.CatFragmentLegacy
import dev.astler.cat_ui.interfaces.ActivityInterface
import dev.astler.cat_ui.interfaces.CoreFragmentInterface
import dev.astler.cat_ui.utils.isAppDarkTheme
import dev.astler.unlib_compose.theme.UnlibComposeTheme

abstract class UnLibComposeCoreFragment : CatFragmentLegacy(), CoreFragmentInterface {

    open var mFragmentTag = "core"
    open var mHasOptionsMenu = false

    override fun onAttach(pContext: Context) {
        super.onAttach(pContext)

        if (pContext is ActivityInterface)
            coreListener = pContext
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(mHasOptionsMenu)

        val nView = ComposeView(requireContext()).apply {
            setContent {
                UnlibComposeTheme(requireContext().isAppDarkTheme()) {
                    ScreenContent()
                }
            }
        }

        return nView
    }

    @Composable
    open fun ScreenContent() {}
}
