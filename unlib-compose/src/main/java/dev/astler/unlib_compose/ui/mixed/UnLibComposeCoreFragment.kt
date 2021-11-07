package dev.astler.unlib_compose.ui.mixed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import dev.astler.unlib.interfaces.ActivityInterface
import dev.astler.unlib.interfaces.CoreFragmentInterface
import dev.astler.unlib.utils.getStringResource
import dev.astler.unlib.utils.isAppDarkTheme
import dev.astler.unlib_compose.theme.UnlibComposeTheme

abstract class UnLibComposeCoreFragment : Fragment(), CoreFragmentInterface {

    open var mFragmentTag = "core"
    open var mHasOptionsMenu = false
    lateinit var coreListener: ActivityInterface

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

    override fun onResume() {
        super.onResume()
        coreListener.setCurrentFragment(this)
    }

    override fun backPressed() {
        coreListener.backPressed()
    }

    fun getStringByName(pName: String, pReturnDef: String = ""): String {
        return requireContext().getStringResource(pName, returnDef = pReturnDef)
    }
}
